package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
import searchengine.dto.statistics.StartIndexingResponse;
import searchengine.model.Site;
import searchengine.repositories.SiteRepository;
import searchengine.services.indexingexecutor.SiteMapCompiler;
import searchengine.services.indexingexecutor.WebPage;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@RequiredArgsConstructor
@Service
public class StartIndexingServiceImpl implements StartIndexingService{

    private final SiteRepository siteRepository;
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

    // 2. заполнение таблиц с использованием ForkJoinPool

        sites.getSites().forEach(site -> {
            System.out.println("=== SITE ===");
            List<String> siteLinks = getUrlList(site.getUrl());
            siteLinks.forEach(System.out::println);
        });

        return startIndexingResponse;
    }

    public List<String> getUrlList (String url){
        int level = 0;
        WebPage rootWebPage = new WebPage(url, level);
        level += 1;
        List<String> urlList = new ForkJoinPool().invoke(new SiteMapCompiler(rootWebPage, level));
        return urlList;
    }

}
