package searchengine.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import searchengine.config.RequestParameters;
import searchengine.config.ConfigSite;
import searchengine.config.SitesList;
import searchengine.dto.statistics.OperationIndexingResponse;
import searchengine.model.Site;
import searchengine.model.Status;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.services.indexingexecutor.SiteMapCompiler;
import searchengine.services.indexingexecutor.ThreadCollector;
import searchengine.services.indexingexecutor.WebPage;
import searchengine.services.stopindexingexecutor.LastErrorMessage;
import searchengine.utils.UrlHandler;

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
    public OperationIndexingResponse getStartIndexing(){
        OperationIndexingResponse operationIndexingResponse;
        if(!checkIsPossibleToRunIndexingProcedure() || !clearTables()) {
            operationIndexingResponse =
                    new OperationIndexingResponse(false, errorMessage);
        } else {
            startIndexingProcedure();
            operationIndexingResponse =
                    new OperationIndexingResponse(true);
        }
        return operationIndexingResponse;
    }

    public void startIndexingProcedure() {
        configSites.getConfigSites().forEach(configSite -> {

            Site site = fillSiteFields(configSite);
            siteRepository.save(site);

            ForkJoinPool forkJoinPool = new ForkJoinPool();

            final Runnable task = () -> {
                fillPageData(site, forkJoinPool);
            };
            final Thread thread = new Thread(task);
            thread.start();
            ThreadCollector.addIndexingThread(thread, forkJoinPool);
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
        site.setUrl(UrlHandler.getPrettyRootUrl(configSite.getUrl()));
        site.setName(configSite.getName());
        return site;
    }

    public void fillPageData(Site site, ForkJoinPool forkJoinPool){
        try {
            WebPage rootWebPage = new WebPage(
                    site,
                    pageRepository,
                    siteRepository,
                    requestParameters
            );
            forkJoinPool.invoke(new SiteMapCompiler(
                    rootWebPage,
                    pageRepository,
                    siteRepository,
                    requestParameters
            ));

            site.setStatus(Status.INDEXED);
            siteRepository.save(site);
        } catch (Exception e) {
            String lastErrorMessage = LastErrorMessage.getLastErrorMessage().isEmpty()
                    ? e.getMessage()
                    : LastErrorMessage.getLastErrorMessage();
            site.setLastError(lastErrorMessage);
            site.setStatus(Status.FAILED);
            site.setStatusTime(LocalDateTime.now());
            siteRepository.save(site);
        }
        ThreadCollector.removeIndexingThread(Thread.currentThread());
    }
}
