package searchengine.services.indexingexecutor;

import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.orm.jpa.JpaSystemException;
import searchengine.config.RequestParameters;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.utils.UrlHandler;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class WebPage{

    private Site site;
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final RequestParameters requestParameters;
    @Getter
    private String url;
    private Document webDocument;
    @Getter
    private List<WebPage> children;
    private final int STATUS_CODE_POSITIVE = 200;
    private final String SAVING_EXCEPTION_MESSAGE = "Sorry, but we couldn't save the page content";

    public WebPage(Site site, PageRepository pageRepository, SiteRepository siteRepository, RequestParameters requestParameters){
        this.site = site;
        this.url = trimLastSlash(site.getUrl());
        this.pageRepository = pageRepository;
        this.siteRepository = siteRepository;
        this.requestParameters = requestParameters;
        try {
            webDocument = Jsoup.connect(url)
                    .userAgent(requestParameters.getUserAgent())
                    .referrer(requestParameters.getReferrer())
                    .get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        children = new ArrayList<>();
    }

    public WebPage(Site site, String url, PageRepository pageRepository, SiteRepository siteRepository, Document webDocument, RequestParameters requestParameters) {
        this.site = site;
        this.url = trimLastSlash(url);
        this.pageRepository = pageRepository;
        this.siteRepository = siteRepository;
        this.requestParameters = requestParameters;
        this.webDocument = webDocument;
        children = new ArrayList<>();
    }

    public void addChildren(){
        Set<String> childrenLinks = getChildrenLinks();
        childrenLinks.forEach(childLink -> {
            Connection.Response response = null;
            String childWebDocumentContent = "";
            try {
                response = Jsoup.connect(childLink)
                        .userAgent(requestParameters.getUserAgent())
                        .referrer(requestParameters.getReferrer())
                        .ignoreHttpErrors(true)
                        .execute();
                if (response.statusCode() == STATUS_CODE_POSITIVE) {
                    Document childWebDocument = response.parse();
                    childWebDocumentContent = childWebDocument.toString();
                    children.add(new WebPage(site, childLink, pageRepository, siteRepository, childWebDocument, requestParameters));
                }
            } catch (Exception e) {
                e.getMessage();
            }
            if(!isThisPageAlreadySaved(getRelativeUrl(childLink))) {
                Page childPage = fillPageFields(response.statusCode(), childLink, childWebDocumentContent);
                try {
                    pageRepository.save(childPage);
                }
                catch (JpaSystemException jse) {
                    childPage.setContent(SAVING_EXCEPTION_MESSAGE);
                    pageRepository.save(childPage);
                }
                changeStatusTime();
            }
        });
    }

    public Set<String> getChildrenLinks(){
        Set<String> childrenLinks = new HashSet<>();
        Elements elements = webDocument.select("a");
        elements.forEach(element -> {
            String urlToAdd;

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

    public Page fillPageFields(int httpCode, String url, String webPageContent) {
        Page page = new Page();
        page.setSite(site);
        page.setPath(getRelativeUrl(url));
        page.setCode(httpCode);
        page.setContent(webPageContent);
        return page;
    }

    public String getRelativeUrl(String fullUrl){
        String path;
        try {
            path = UrlHandler.getPathFromUrl(fullUrl);
        } catch (MalformedURLException e) {
            path = fullUrl.replace(site.getUrl(), "");
        }
        return path;
    }

    public boolean isThisPageAlreadySaved(String url){
        // 3 TODO тут, пожалуй надо доработать, т.к. поиск должен быть по Path и Site
        List<Page> savedPageWithUrl = pageRepository.findByPathAndSite(url, site);
        return savedPageWithUrl.isEmpty() ? false : true;
    }

    public void changeStatusTime(){
        site.setStatusTime(LocalDateTime.now());
        siteRepository.save(site);
    }
}
