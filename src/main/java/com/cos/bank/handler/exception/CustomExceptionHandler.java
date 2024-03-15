package com.cos.bank.handler.exception;

import com.cos.bank.handler.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiExceptionHandler(CustomApiException e){
        logger.error(e.getMessage());
        // return response object and status code
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomForbiddenException.class)
    public ResponseEntity<?> forbiddenExceptionHandler(CustomForbiddenException e){
        logger.error(e.getMessage());
        // return response object and status code
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> validationExceptionHandler(CustomValidationException e){
        logger.error(e.getMessage());
        // return response object and status code
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }
}
