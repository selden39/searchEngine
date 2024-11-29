package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.RequestParameters;
import searchengine.config.SitesList;
import searchengine.dto.statistics.StartIndexingResponse;
import searchengine.model.Site;
import searchengine.model.Status;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.services.indexingexecutor.SiteMapCompiler;
import searchengine.services.indexingexecutor.WebPage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@RequiredArgsConstructor
@Service
public class StartIndexingServiceImpl implements StartIndexingService{

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final RequestParameters requestParameters;
    private final SitesList sites;

    @Override
    public StartIndexingResponse getStartIndexing(){
        StartIndexingResponse startIndexingResponse =
                new StartIndexingResponse(true);

    // 0. Отладочная информация
        System.out.println("=== print table before indexing ===");
        Collection<Site> currentSites = siteRepository.findAll();
        currentSites.forEach(siteX -> {
            System.out.println(siteX.getId() + " - " + siteX.getName() + " - " + siteX.getUrl());
        });

        System.out.println("=== print configured sites ===");
        sites.getSites().forEach(site -> {
            System.out.println(site.getName() + " :  " + site.getUrl());
        });

    // 1. очистить таблицы site and page
        siteRepository.deleteAll();
        pageRepository.deleteAll();

    // 2. заполнение таблиц с использованием ForkJoinPool
        //TODO обработку каждого сайта нужно запустить в отдельном потоке

        sites.getSites().forEach(siteFromConfig -> {
            System.out.println("=== SITE ===");
            Site site = new Site();
            site.setStatus(Status.INDEXING);
            site.setStatusTime(LocalDateTime.now());
            site.setUrl(siteFromConfig.getUrl());
            site.setName(siteFromConfig.getName());
            siteRepository.save(site);
            List<String> siteLinks = getUrlList(site);
            siteLinks.forEach(System.out::println);
        });

        return startIndexingResponse;
    }

    public List<String> getUrlList (Site site){
        WebPage rootWebPage = new WebPage(site, pageRepository, siteRepository, requestParameters);
        List<String> urlList = new ForkJoinPool()
                .invoke(new SiteMapCompiler(rootWebPage, pageRepository, siteRepository, requestParameters));
        site.setStatus(Status.INDEXED);
        siteRepository.save(site);
        return urlList;
    }

}
