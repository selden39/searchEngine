package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
import searchengine.dto.IndexPage;
import searchengine.dto.statistics.OperationIndexingResponse;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class IndexPageServiceImpl implements IndexPageService{

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final SitesList configSites;
    private final String ERROR_DESC_OUT_OF_SITE_LIST = "Данная страница находится за пределами сайтов, \n" +
            "указанных в конфигурационном файле";
    private final String ERROR_DESC_PAGE_NOT_FOUND = "Страница не найдена";

    @Override
    public OperationIndexingResponse postIndexPage(IndexPage indexPage) {
        OperationIndexingResponse operationIndexingResponse;

        // проверка, что сайт наш
        if (!isPageFromConfigSites(indexPage)){
            operationIndexingResponse = new OperationIndexingResponse(false, ERROR_DESC_OUT_OF_SITE_LIST);
            return operationIndexingResponse;
        }

        // получение html
        try {
            String html = getIndexPageHtml(indexPage);
        } catch (IOException e) {
            operationIndexingResponse = new OperationIndexingResponse(false, ERROR_DESC_PAGE_NOT_FOUND);
            return operationIndexingResponse;
        }

        // добавлен ли сайт? -> добавить


        // добавлена ли страница -> добавить страницу и леммы
        // проиндексирована ли страница -> очистить page, lemma, index -> добавить страницу и леммы

        // отправка ответа
        isPageFromConfigSites(indexPage);

        return null;
    }

    private boolean isPageFromConfigSites(IndexPage indexPage){
        return configSites.getConfigSites().stream()
                .map(configSite -> configSite.getUrl().toLowerCase())
                .anyMatch(configSiteUrl -> {
                    if (indexPage.getUrl().toLowerCase().indexOf(configSiteUrl) == 0){
                        return true;
                    } else {
                        return false;
                    }
                });
    }

    private String getIndexPageHtml(IndexPage indexPage) throws IOException {
        Document webDocument = Jsoup.connect(indexPage.getUrl()).get();
        return webDocument.toString();
    }
}
