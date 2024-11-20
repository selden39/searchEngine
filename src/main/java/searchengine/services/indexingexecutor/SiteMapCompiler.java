package searchengine.services.indexingexecutor;

import searchengine.repositories.PageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SiteMapCompiler extends RecursiveTask<List<String>> {
    private WebPage webPage;
    int level;  // TODO убрать левел. т.к. он использовался только для преттиУрл
    private final int PAUSE_BEFORE_TAKE_NEXT_CHILD = 200;
    private PageRepository pageRepository;

    public SiteMapCompiler(WebPage webPage, int level, PageRepository pageRepository){
        this.level = level;
        this.webPage = webPage;
        this.pageRepository = pageRepository;
        this.webPage.addChildren(level);
    }

    @Override
    protected List<String> compute() {
        List<String> urlList = new ArrayList<>();
        urlList.add(webPage.getPrettyUrl());
        level += 1;
        List<SiteMapCompiler> taskList = new ArrayList<>();
        for(WebPage child : webPage.getChildren()){
            SiteMapCompiler childTask = new SiteMapCompiler(child, level, pageRepository);
            childTask.fork();
            taskList.add(childTask);
            try {
                Thread.sleep(PAUSE_BEFORE_TAKE_NEXT_CHILD);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for(SiteMapCompiler task : taskList){
            urlList.addAll(task.join());
        }

        return urlList;
    }
}

