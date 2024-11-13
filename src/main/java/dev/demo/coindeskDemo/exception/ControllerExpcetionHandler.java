package dev.demo.coindeskDemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExpcetionHandler {

    // 處理一般的 Exception
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleAllExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>("系統錯誤，請稍後再試。", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 處理CurrencyException
    @ExceptionHandler(CurrencyException.class)
    public final ResponseEntity<String> handleNotFoundException(CurrencyException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
