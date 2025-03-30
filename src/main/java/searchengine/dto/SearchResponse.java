package searchengine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResponse {
    private boolean result;
    private String query;
    private String searchSite;
    private Integer offset;
    private Integer limit;

    public SearchResponse(boolean result, String query, String searchSite, Integer offset, Integer limit){
        this.result = result;
        this.query = query;
        this.searchSite = searchSite;
        this.offset = offset;
        this.limit = limit;
    }
}
