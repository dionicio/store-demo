package com.store.demo.exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /** Provides handling for exceptions throughout this service. */
    @ExceptionHandler({ ItemNotFoundException.class,DuplicatedException.class, ServerException.class ,ItemRequiredException.class})
    public final ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof ItemRequiredException) {           
            return handleException((ItemRequiredException) ex, headers, HttpStatus.BAD_REQUEST, request);
        }else
        if (ex instanceof ServerException) {  
        	log.error(ex.getMessage());
            return handleException((ServerException) ex, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }else
        if (ex instanceof ItemNotFoundException) {
            return handleException((ItemNotFoundException) ex, headers, HttpStatus.NOT_FOUND, request);
        
        }else if (ex instanceof DuplicatedException) {           
            return handleException((DuplicatedException) ex, headers, HttpStatus.CONFLICT, request);
        }else 
         {
        	log.error(ex.getMessage());
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }

    protected ResponseEntity<ApiError> handleException(RuntimeException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(LocalDateTime.now(),"ERROR",errors), headers, status, request);
    }

    

    /** A single place to customize the response body of all Exception types. */
    protected ResponseEntity<ApiError> handleExceptionInternal(Exception ex, ApiError body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}