package searchengine.services;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ServiceValidationException extends Exception{
    private int httpCode;
    private boolean httpResult;
    private String errorDesc;
    private LocalDateTime errorDateTime;
    private int DEFAULT_HTTP_CODE = 500;

    public ServiceValidationException(int httpCode, boolean httpResult, String errorDesc, LocalDateTime errorDateTime) {
        this.httpCode = httpCode;
        this.httpResult = httpResult;
        this.errorDesc = errorDesc;
        this.errorDateTime = errorDateTime;
    }

    public ServiceValidationException(boolean httpResult, String errorDesc){
        this.httpCode = DEFAULT_HTTP_CODE;
        this.httpResult = httpResult;
        this.errorDesc = errorDesc;
        this.errorDateTime = LocalDateTime.now();
    }

    public ServiceValidationException(int httpCode, boolean httpResult, String errorDesc){
        this.httpCode = httpCode;
        this.httpResult = httpResult;
        this.errorDesc = errorDesc;
        this.errorDateTime = LocalDateTime.now();
    }


}
