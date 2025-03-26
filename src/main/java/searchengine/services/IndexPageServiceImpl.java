package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.config.ConfigSite;
import searchengine.config.RequestParameters;
import searchengine.config.SitesList;
import searchengine.dto.IndexPage;
import searchengine.dto.statistics.OperationIndexingResponse;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.model.Status;
import searchengine.repositories.IndexRepository;
import searchengine.repositories.LemmaRepository;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.services.lemmatization.LemmasDataRemover;
import searchengine.services.lemmatization.LemmasDataSaver;
import searchengine.utils.UrlHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IndexPageServiceImpl implements IndexPageService{

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;
    private final SitesList configSites;
    private final RequestParameters requestParameters;
    private Page page;
    private final String ERROR_DESC_OUT_OF_SITE_LIST = "Данная страница находится за пределами сайтов, \n" +
            "указанных в конфигурационном файле";
    private final String ERROR_DESC_PAGE_NOT_FOUND = "Страница не найдена";
    private final String ERROR_LEMMATIZATION = "Не удалось выполнить сбор и сохранение лемм";
    private final String ERROR_REMOVE_LEMMAS_DATA = "Не удалось выполнить удаление \"старой\" информации для этой страницы";

    @Override
    public OperationIndexingResponse postIndexPage(IndexPage indexPage) {
        page = new Page();

        // проверка, что сайт наш и сразу возвращаем этот сайт, чтобы его потом создать, если не нашли в базе
        Optional<ConfigSite> configSiteForIndexPage = getConfigSiteByIndexPage(indexPage);
        if(!configSiteForIndexPage.isPresent()){
            return new OperationIndexingResponse(false, ERROR_DESC_OUT_OF_SITE_LIST);
        }

     // получение html и statusCode
        try {
            fillIndexPageHtmlAndStatusCode(indexPage);
        } catch (IOException e) {
            return new OperationIndexingResponse(false, ERROR_DESC_PAGE_NOT_FOUND);
        }

     // добавлен ли сайт? -> добавить и добавить сайт в page
        List<Site> repositorySitesByIndexPage = getSiteFromSiteRepository(indexPage);
        Site repositorySiteByIndexPage;
        if(repositorySitesByIndexPage.isEmpty()) {
            repositorySiteByIndexPage = saveIndexPageSite(configSiteForIndexPage.get());
        } else {
            repositorySiteByIndexPage = repositorySitesByIndexPage.get(0);
        }

        // добавлена ли страница:
        // да -> удалить страницу и леммы
        // добавить страницу и леммы

        List<Page> repositoryPages = getRepositoryPageByIndexPage(indexPage, repositorySiteByIndexPage);
        if (!repositoryPages.isEmpty()) {
            //удаление лемм и индексов

            LemmasDataRemover lemmasDataRemover = new LemmasDataRemover(repositoryPages,
                    repositorySiteByIndexPage, lemmaRepository, indexRepository);
            try {
                lemmasDataRemover.removeLemmasData();
            } catch (Exception e){
                return new OperationIndexingResponse(false, ERROR_REMOVE_LEMMAS_DATA);
            }

            deleteRepositoryPage(repositoryPages);
        }

        page.setSite(repositorySiteByIndexPage);
        page.setPath(UrlHandler.getPathFromUrl(indexPage.getUrl()));
        pageRepository.save(page);
        // лемматизация (таблицы lemma + index)
        LemmasDataSaver lemmasDataSaver = new LemmasDataSaver(repositorySiteByIndexPage,
                page, lemmaRepository, indexRepository);
        try {
            lemmasDataSaver.saveLemmasData();
        } catch (Exception e) {
            return new OperationIndexingResponse(false, ERROR_LEMMATIZATION);
        }


        // если repositoryPage не пусто, то
            // проиндексирована ли страница -> очистить page, lemma, index -> добавить страницу и леммы

        // отправка ответа
// TODO доработать ответ под требования
        return new OperationIndexingResponse(true);
    }

    private Optional<ConfigSite> getConfigSiteByIndexPage(IndexPage indexPage){
        return configSites.getConfigSites().stream()
                .filter(configSite ->
                    UrlHandler.getPrettyRootUrl(configSite.getUrl()).equals(UrlHandler.getPrettyRootUrl(indexPage.getUrl())))
                .findFirst();
    }

    private void fillIndexPageHtmlAndStatusCode(IndexPage indexPage) throws IOException {
        Connection.Response response = Jsoup.connect(indexPage.getUrl())
                .userAgent(requestParameters.getUserAgent())
                .referrer(requestParameters.getReferrer())
                .ignoreHttpErrors(true)
                .execute();
        Document webDocument = response.parse();
        page.setCode(response.statusCode());
        page.setContent(webDocument.toString());
    }

    private List<Site> getSiteFromSiteRepository(IndexPage indexPage){
        return siteRepository.findByUrl(UrlHandler.getPrettyRootUrl(indexPage.getUrl()));
    }

    private Site saveIndexPageSite (ConfigSite configSiteForIndexPage){
        Site site = fillSiteFields(configSiteForIndexPage);
        return siteRepository.save(site);
    }

    private Site fillSiteFields(ConfigSite configSite) {
        Site site = new Site();
        site.setStatus(Status.FAILED);
        site.setStatusTime(LocalDateTime.now());
        site.setUrl(UrlHandler.getPrettyRootUrl(configSite.getUrl()));
        site.setName(configSite.getName());
        return site;
    }

    private List<Page> getRepositoryPageByIndexPage(IndexPage indexPage, Site repositorySitesByIndexPage) {
        return pageRepository.findByPathAndSite(
                UrlHandler.getPathFromUrl(indexPage.getUrl())
                , repositorySitesByIndexPage);
    }

    private void deleteRepositoryPage(List<Page> repositoryPages){
        pageRepository.deleteAll(repositoryPages);
    }
}
