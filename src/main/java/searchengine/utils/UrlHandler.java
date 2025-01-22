package searchengine.utils;

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
}
