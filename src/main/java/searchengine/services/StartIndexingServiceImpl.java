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
import java.util.concurrent.ForkJoinPool;

@RequiredArgsConstructor
@Service
public class StartIndexingServiceImpl implements StartIndexingService{

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final RequestParameters requestParameters;
    private final SitesList configSites;

    @Override
    public StartIndexingResponse getStartIndexing(){

        clearTables();

        //TODO обработку каждого сайта нужно запустить в отдельном потоке
        configSites.getConfigSites().forEach(configSite -> {
            Site site = fillSiteFields(configSite);
            siteRepository.save(site);
            fillPageData(site);
        });

        StartIndexingResponse startIndexingResponse =
                new StartIndexingResponse(true);
        return startIndexingResponse;
    }

    public void clearTables(){
        siteRepository.deleteAll();
        pageRepository.deleteAll();
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
