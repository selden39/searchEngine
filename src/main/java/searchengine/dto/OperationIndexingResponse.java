package searchengine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationIndexingResponse {
    private boolean result;
    @JsonProperty("error")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String statusDescription;

    public OperationIndexingResponse(boolean result, String statusDescription){
        this.result = result;
        this.statusDescription = statusDescription;
    }

    public OperationIndexingResponse(boolean result) {
        this.result = result;
        this.statusDescription = null;
    }
}
