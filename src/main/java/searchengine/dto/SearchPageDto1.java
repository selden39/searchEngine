package searchengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SearchPageDto1 {
    private int id;
    private String url;
    private int siteId;

    public SearchPageDto1(int id, String url, int siteId) {
        this.id = id;
        this.url = url;
        this.siteId = siteId;
    }
}
