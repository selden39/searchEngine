package searchengine.dto.statistics;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartIndexingResponse {
    private boolean result;
    private String statusDescription;

    public StartIndexingResponse(boolean result, String errorDescription){
        this.result = result;
        this.statusDescription = errorDescription;
    }

    public StartIndexingResponse(boolean result) {
        this.result = result;
        this.statusDescription = null;
    }
}
