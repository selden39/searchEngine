package searchengine.services.indexingexecutor;


import lombok.Getter;
import org.jsoup.Connection;
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

public class WebPage{

    private Site site;
    private PageRepository pageRepository;
    @Getter
    private String url;
    private Document webDocument;
    @Getter
    private List<WebPage> children;
    private final int STATUS_CODE_POSITIVE = 200;

    public WebPage(Site site, String url, PageRepository pageRepository){
        //TODO подумать насчет переменных site, rootUrl, url - вск ли они нужны?
        this.site = site;
        this.url = trimLastSlash(url);
        this.pageRepository = pageRepository;
        try {
            webDocument = Jsoup.connect(url).get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        children = new ArrayList<>();
    }

    public WebPage(Site site, PageRepository pageRepository){
        this(site, site.getUrl(), pageRepository);
    }

    public WebPage(Site site, String url, PageRepository pageRepository, Document webDocument) {
        this.site = site;
        this.url = trimLastSlash(url);
        this.pageRepository = pageRepository;
        this.webDocument = webDocument;
        children = new ArrayList<>();
    }

    public void addChildren(){
        Set<String> childrenLinks = getChildrenLinks();
        childrenLinks.forEach(childLink -> {
            Connection.Response response = null;
            String childWebDocumentContent = "";
            try {
                response = Jsoup.connect(childLink).ignoreHttpErrors(true).execute();
                if (response.statusCode() == STATUS_CODE_POSITIVE) {
                    Document childWebDocument = response.parse();
                    childWebDocumentContent = childWebDocument.toString();
                    children.add(new WebPage(site, childLink, pageRepository, childWebDocument));
                }
            } catch (Exception e) {
                e.getMessage();
            }
            savePage(response.statusCode(), childLink, childWebDocumentContent);
        });
    }

    public Set<String> getChildrenLinks(){
        Set<String> childrenLinks = new HashSet<>();
        Elements elements = webDocument.select("a");
        elements.forEach(element -> {
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
                urlToAdd = site.getUrl() + trimLastSlash(element.attr("href"));
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

    //TODO подумать может вынести куда-то в другое место
    public void savePage(int httpCode, String url, String webPageContent){
        Page page = new Page();
        page.setSite(site);
        page.setPath(url.replace(site.getUrl(), ""));
        page.setCode(httpCode);
        page.setContent(webPageContent);

        try {
            pageRepository.save(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
