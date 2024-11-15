package searchengine.services.indexingexecutor;


import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.PageRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebPage {

    private Site site;
    private String rootUrl;
    private PageRepository pageRepository;
    private String url;
    private final String prefix = "    ";
    @Getter
    private String prettyUrl;
    private Document webPage;
    @Getter
    private List<WebPage> children;

    public WebPage(Site site, String url, int level, PageRepository pageRepository){
        this.site = site;
        this.rootUrl = trimLastSlash(site.getUrl());
        this.url = trimLastSlash(url);
        this.pageRepository = pageRepository;
        prettyUrl = prefix.repeat(level) + url;
        try {
            webPage = Jsoup.connect(url).get();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
        children = new ArrayList<>();
        savePage();
    }

    public WebPage(Site site, int level, PageRepository pageRepository){
        this(site, site.getUrl(), level, pageRepository);
    }

    public void addChildren(int level){
        getChildrenLinks().forEach(childLink -> {
            //если по полученной ссылке не удалось получить страницу, то не добавляем такого chaild
            //такая ситуация может произойти при https://lenta.ru/articles/2024/03/23/crocus/ -> https://lenta.ru/articles/
            try {
                Jsoup.connect(childLink).get();
                children.add(new WebPage(site, childLink, level, pageRepository));
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println(e.getMessage());
            }
        });
    }

    public Set<String> getChildrenLinks(){
        Set<String> childrenLinks = new HashSet<>();
        Elements elements = webPage.select("a");
        elements.forEach(element -> {
            //System.out.println("RAW href: " + element.attr("href"));
            String urlToAdd;
            /*
            1. с учетом примера https://www.youtube.com/ решено в дочерние вклчить не только относительные,
                но и абсолютные ссылки
            2. при этом в ситуации когда у корневой есть дочерние 2 уровня,
                https://lenta.ru/  ->   https://lenta.ru/articles/263 добавляем дочернюю https://lenta.ru/articles
                на этапе попытке получения такой страницы или узнаем, что
                такая сттраница дествительно есть или получаем ошибку -> не добавляем в children
             */

            // если ссылка относительная, то обогащаем до абсолтной
            String regexpRelative = "^\\/\\S{1,}"; // относительные, некорневые ссылки
            if(element.attr("href").matches(regexpRelative)){
                urlToAdd = rootUrl + trimLastSlash(element.attr("href"));
            } else {
                urlToAdd = element.attr("href");
            }

            String regexpAbs = url.replace("/", "\\/") + "\\/[^\\/?]{1,}"; // ищем абсолютные с таким же началом как и URL и до первого / или ?
            Pattern pattern = Pattern.compile(regexpAbs);
            Matcher matcher = pattern.matcher(urlToAdd);
            while (matcher.find()) {
                childrenLinks.add(matcher.group());
            }
        });
        return childrenLinks;
    }

    public String trimLastSlash(String url){
        if(url.endsWith("/")){
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    public void savePage(){
        Page page = new Page();
        page.setSite(site);
        page.setPath(url);
        page.setCode(200);
        page.setContent(prettyUrl);
        pageRepository.save(page);
    }
}