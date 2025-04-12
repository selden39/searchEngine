package searchengine.dto;

public class SearchResponseDto1 {
    private int id;
    private String url;
    private int siteId;

    public SearchResponseDto1(int id, String url, int siteId) {
        this.id = id;
        this.url = url;
        this.siteId = siteId;
    }
}
