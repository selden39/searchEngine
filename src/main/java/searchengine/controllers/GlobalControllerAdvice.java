package searchengine.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import searchengine.dto.ErrorMessageResponse;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessageResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorMessageResponse(
                        false,
                        "Выбранный метод недоступен для указанной строки запроса")
                );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessageResponse> handleMissingServletRequestParameterException (MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorMessageResponse(
                        false,
                        "Некорректный запрос. Пропущен обязательный параметр")
                );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessageResponse> handleConstraintViolationException (ConstraintViolationException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorMessageResponse(
                        false,
                        "Параметры запроса указаны некорректно")
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
