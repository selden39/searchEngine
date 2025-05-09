package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.config.ConfigSite;
import searchengine.config.RequestParameters;
import searchengine.config.SitesList;
import searchengine.dto.IndexPageRequest;
import searchengine.dto.OperationIndexingResponse;
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
    private final String ERROR_DESC_OUT_OF_SITE_LIST = "Данная страница находится за пределами сайтов, " +
            "указанных в конфигурационном файле";
    private final String ERROR_DESC_PAGE_NOT_FOUND = "Страница не найдена";
    private final String ERROR_LEMMATIZATION = "Не удалось выполнить сбор и сохранение лемм";
    private final String ERROR_REMOVE_LEMMAS_DATA = "Не удалось выполнить удаление \"старой\" информации для этой страницы";
    private final String ERROR_IOEXCEPTION = "Внутренняя ошибка при обработке данных страницы";

    @Override
    public OperationIndexingResponse postIndexPage(IndexPageRequest indexPageRequest) throws ServiceValidationException{
        page = new Page();

        Optional<ConfigSite> configSiteForIndexPage = getConfigSiteByIndexPage(indexPageRequest);
        if(!configSiteForIndexPage.isPresent()){
            throw new ServiceValidationException(406, false, ERROR_DESC_OUT_OF_SITE_LIST);
        }

        try {
            fillIndexPageHtmlAndStatusCode(indexPageRequest);
            if (page.getCode() >= 400) {
                throw new ServiceValidationException(404, false, ERROR_DESC_PAGE_NOT_FOUND);
            }
        } catch (IOException e) {
            throw new ServiceValidationException(false, ERROR_IOEXCEPTION);
        }

        List<Site> repositorySitesByIndexPage = getSiteFromSiteRepository(indexPageRequest);
        Site repositorySiteByIndexPage;
        if(repositorySitesByIndexPage.isEmpty()) {
            repositorySiteByIndexPage = saveIndexPageSite(configSiteForIndexPage.get());
        } else {
            repositorySiteByIndexPage = repositorySitesByIndexPage.get(0);
        }

        List<Page> repositoryPages = getRepositoryPageByIndexPage(indexPageRequest, repositorySiteByIndexPage);
        if (!repositoryPages.isEmpty()) {

            LemmasDataRemover lemmasDataRemover = new LemmasDataRemover(repositoryPages,
                    repositorySiteByIndexPage, lemmaRepository, indexRepository);
            try {
                lemmasDataRemover.removeLemmasData();
            } catch (Exception e){
                throw new ServiceValidationException(false, ERROR_REMOVE_LEMMAS_DATA);
            }

            deleteRepositoryPage(repositoryPages);
        }

        page.setSite(repositorySiteByIndexPage);
        page.setPath(UrlHandler.getPathFromUrl(indexPageRequest.getUrl()));
        pageRepository.save(page);

        LemmasDataSaver lemmasDataSaver = new LemmasDataSaver(repositorySiteByIndexPage,
                page, lemmaRepository, indexRepository);
        try {
            lemmasDataSaver.saveLemmasData();
        } catch (Exception e) {
            throw new ServiceValidationException(false, ERROR_LEMMATIZATION);
        }

        return new OperationIndexingResponse(true);
    }

    private Optional<ConfigSite> getConfigSiteByIndexPage(IndexPageRequest indexPageRequest){
        return configSites.getConfigSites().stream()
                .filter(configSite ->
                    UrlHandler.getPrettyRootUrl(configSite.getUrl()).equals(UrlHandler.getPrettyRootUrl(indexPageRequest.getUrl())))
                .findFirst();
    }

    private void fillIndexPageHtmlAndStatusCode(IndexPageRequest indexPageRequest) throws IOException {
        Connection.Response response = Jsoup.connect(indexPageRequest.getUrl())
                .userAgent(requestParameters.getUserAgent())
                .referrer(requestParameters.getReferrer())
                .ignoreHttpErrors(true)
                .execute();
        Document webDocument = response.parse();
        page.setCode(response.statusCode());
        page.setContent(webDocument.toString());
    }

    private List<Site> getSiteFromSiteRepository(IndexPageRequest indexPageRequest){
        return siteRepository.findByUrl(UrlHandler.getPrettyRootUrl(indexPageRequest.getUrl()));
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

    private List<Page> getRepositoryPageByIndexPage(IndexPageRequest indexPageRequest, Site repositorySitesByIndexPage) {
        return pageRepository.findByPathAndSite(
                UrlHandler.getPathFromUrl(indexPageRequest.getUrl())
                , repositorySitesByIndexPage);
    }

    private void deleteRepositoryPage(List<Page> repositoryPages){
        pageRepository.deleteAll(repositoryPages);
    }
}
