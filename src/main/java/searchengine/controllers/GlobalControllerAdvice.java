package searchengine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import searchengine.dto.ErrorMessageResponse;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessageResponse> handleMissingServletRequestParameterException (MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorMessageResponse(
                        false,
                        "Некорректный запрос. Пропущен обязательный параметр")
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleException (Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError()
                .body(new ErrorMessageResponse(false, "Произошла непредвиденная ошибка")
                );
    }

}
