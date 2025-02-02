package searchengine.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlHandler {

    public static String getPrettyRootUrl(String url) {
        String prettyUrl = url.trim();

        int countSlash = prettyUrl.length() - prettyUrl.replace("/", "").length();
        while (countSlash > 2) {
            prettyUrl = prettyUrl.substring(0, prettyUrl.lastIndexOf('/'));
            countSlash -= 1;
        }

        return prettyUrl.toLowerCase();
    }

    public static String getPathFromUrl (String url) {
        String path;
        try {
            path = new URL(url).getPath();
        }catch (MalformedURLException e) {
            path = url.replace(getPrettyRootUrl(url), "");
        }
        if(path.isEmpty()) {
            path = "/";
        } else {
            path = path.endsWith("/")
                    ? path.substring(0, path.length() - 1)
                    : path;
        }
        return path;
    }
}
