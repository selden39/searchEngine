package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.RequestParameters;
import searchengine.config.ConfigSite;
import searchengine.config.SitesList;
import searchengine.dto.statistics.StartIndexingResponse;
import searchengine.model.Site;
import searchengine.model.Status;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.services.indexingexecutor.SiteMapCompiler;
import searchengine.services.indexingexecutor.WebPage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@RequiredArgsConstructor
@Service
public class StartIndexingServiceImpl implements StartIndexingService{

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final RequestParameters requestParameters;
    private final SitesList configSites;
    private String errorMessage;
    private final String ERROR_DESC_HAS_ALREADY_RUNNING =  "Индексация не запущена, т.к. процедура индексация уже запущена и не завершена";
    private final String ERROR_DESC_DELETING_ERROR = "Индексация не запущена, т.к. возникла ошибка при удалении данных";
    private final String ERROR_DESC_STATUS_GETTING_ERROR =  "Индексация не запущена, т.к. возникли проблемы с получением текущих статусов сайтов";

    @Override
    public StartIndexingResponse getStartIndexing(){
        StartIndexingResponse startIndexingResponse;
        if(!checkIsPossibleToRunIndexingProcedure() || !clearTables()) {
            System.out.println("===== Indexing procedure is already running =====");
            startIndexingResponse =
                    new StartIndexingResponse(false, errorMessage);
       /* } else if (!clearTables()){
            System.out.println("===== Problem with deleting =====");
            startIndexingResponse =
                    new StartIndexingResponse(false, error_message);

        */
        } else {
            System.out.println("===== New indexing procedure starts =====");
            startIndexingProcedure();
            startIndexingResponse =
                    new StartIndexingResponse(true);
        }
        return startIndexingResponse;
    }

    public void startIndexingProcedure() {
        //TODO обработку каждого сайта нужно запустить в отдельном потоке
        configSites.getConfigSites().forEach(configSite -> {
            Site site = fillSiteFields(configSite);
            siteRepository.save(site);
            fillPageData(site);
        });
    }

    public boolean checkIsPossibleToRunIndexingProcedure(){
        try {
            boolean result = true;
            List<Site> indexingSites = siteRepository.findByStatus(Status.INDEXING);
            if (!indexingSites.isEmpty()) {
                errorMessage = ERROR_DESC_HAS_ALREADY_RUNNING;
                result = false;
            }
            return result;
        } catch (Exception e) {
            errorMessage = ERROR_DESC_STATUS_GETTING_ERROR;
            return false;
        }
    }

    public boolean clearTables() {
        boolean result = true;
        try {
            siteRepository.deleteAll();
            pageRepository.deleteAll();
        } catch (Exception e) {
            errorMessage = ERROR_DESC_DELETING_ERROR;
            result = false;
        }
        return result;
    }

    public Site fillSiteFields(ConfigSite configSite) {
        Site site = new Site();
        site.setStatus(Status.INDEXING);
        site.setStatusTime(LocalDateTime.now());
        site.setUrl(configSite.getUrl());
        site.setName(configSite.getName());
        return site;
    }

    public void fillPageData(Site site){
        try {
            WebPage rootWebPage = new WebPage(
                    site,
                    pageRepository,
                    siteRepository,
                    requestParameters
            );
            new ForkJoinPool().invoke(new SiteMapCompiler(
                    rootWebPage,
                    pageRepository,
                    siteRepository,
                    requestParameters
            ));

            site.setStatus(Status.INDEXED);
            siteRepository.save(site);
        } catch (Exception e) {
            site.setLastError(e.getMessage());
            site.setStatus(Status.FAILED);
            site.setStatusTime(LocalDateTime.now());
            siteRepository.save(site);
        }
    }
}
