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
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.utils.UrlHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IndexPageServiceImpl implements IndexPageService{

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final SitesList configSites;
    private final RequestParameters requestParameters;
    private Page page = new Page();
    private final String ERROR_DESC_OUT_OF_SITE_LIST = "Данная страница находится за пределами сайтов, \n" +
            "указанных в конфигурационном файле";
    private final String ERROR_DESC_PAGE_NOT_FOUND = "Страница не найдена";
    private final String ERROR_DESC_GET_PATH_ERROR = "Не удалось получить путь для указанного URL";

    @Override
    public OperationIndexingResponse postIndexPage(IndexPage indexPage) {

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

        // добавлена ли страница -> добавить страницу и леммы
        List<Page> repositoryPage;
        try {
            repositoryPage = getRepositoryPageByIndexPage(indexPage, repositorySiteByIndexPage);
        } catch (MalformedURLException e) {
            return new OperationIndexingResponse(false, ERROR_DESC_GET_PATH_ERROR);
        }

        if (repositoryPage.isEmpty()){
            page.setSite(repositorySiteByIndexPage);
            page.setPath(UrlHandler.getPathFromUrl(indexPage.getUrl()));
            pageRepository.save(page);
            // лемматизация (таблицы lemma + index)

        }

        // если repositoryPage не пусто, то
            // проиндексирована ли страница -> очистить page, lemma, index -> добавить страницу и леммы

        // отправка ответа

        return null;
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

    public Site fillSiteFields(ConfigSite configSite) {
        Site site = new Site();
        site.setStatus(Status.FAILED);
        site.setStatusTime(LocalDateTime.now());
        site.setUrl(UrlHandler.getPrettyRootUrl(configSite.getUrl()));
        site.setName(configSite.getName());
        return site;
    }

    public List<Page> getRepositoryPageByIndexPage(IndexPage indexPage, Site repositorySitesByIndexPage) throws MalformedURLException {
        return pageRepository.findByPathAndSite(
                UrlHandler.getPathFromUrl(indexPage.getUrl())
                , repositorySitesByIndexPage);
    }
}
