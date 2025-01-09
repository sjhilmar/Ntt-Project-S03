package com.example.ms_bankcredit.exception;

import com.example.ms_bankcredit.exception.dto.ErrorResponse;
import com.example.ms_bankcredit.util.CustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex){
        log.error("Business exception: {}",ex.getMessage(),ex);
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                "Custom Business Violation"
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex){
        log.error("Response status exception: {}",ex.getMessage(),ex);
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ex.getReason(),
                ex.getStatusCode().value(),
                "Response status violation"
        );
        return new ResponseEntity<>(errorResponse,ex.getStatusCode());
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.error("Unhandled Exception: {}",ex.getMessage(),ex);
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error"
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WebClientResponseException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleWebClientResponseException(WebClientResponseException ex){
        log.error("WebClient response exception: {}",ex.getMessage(),ex);
        ErrorResponse errorResponse= new ErrorResponse(LocalDateTime.now(),
                ex.getMessage(),
                ex.getStatusCode().value(),"WebClient Error");
        return new ResponseEntity<>(errorResponse,ex.getStatusCode());
    }
}
