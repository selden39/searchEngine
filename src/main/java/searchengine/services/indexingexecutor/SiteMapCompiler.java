package searchengine.services.indexingexecutor;

import searchengine.config.RequestParameters;
import searchengine.repositories.PageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SiteMapCompiler extends RecursiveTask<List<String>> {
    private WebPage webPage;
    private final int PAUSE_BEFORE_TAKE_NEXT_CHILD = 200;
    private PageRepository pageRepository;
    private final RequestParameters requestParameters;

    public SiteMapCompiler(WebPage webPage, PageRepository pageRepository, RequestParameters requestParameters){
        this.webPage = webPage;
        this.pageRepository = pageRepository;
        this.requestParameters = requestParameters;
        this.webPage.addChildren();
    }

    @Override
    protected List<String> compute() {
        List<String> urlList = new ArrayList<>();
        urlList.add(webPage.getUrl());
        List<SiteMapCompiler> taskList = new ArrayList<>();
        webPage.getChildren().forEach(child -> {
            SiteMapCompiler childTask = new SiteMapCompiler(child, pageRepository, requestParameters);
            childTask.fork();
            taskList.add(childTask);

            try {
                Thread.sleep(PAUSE_BEFORE_TAKE_NEXT_CHILD);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        for(SiteMapCompiler task : taskList){
            urlList.addAll(task.join());
        }
        return urlList;
    }
}

