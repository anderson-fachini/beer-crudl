package com.fachini.beercrudl.controllers.handlers;

import static com.fachini.beercrudl.config.DefaultMessageSourceConfig.DEFAULT_LOCALE;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String JSON_MALFORMED = "json.malformed";
    private final MessageSource messageSource;
    private final ValidationExceptionHandler validationExceptionHandler;

    public GlobalExceptionHandler(MessageSource messageSource, ValidationExceptionHandler validationExceptionHandler) {
        this.messageSource = messageSource;
        this.validationExceptionHandler = validationExceptionHandler;
    }

    @Override
    public final ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final String message = messageSource.getMessage(JSON_MALFORMED, null, DEFAULT_LOCALE);
        if (exception.getCause() instanceof InvalidFormatException) {
            return validationExceptionHandler.handleInvalidFormatException((InvalidFormatException) exception.getCause());
        }
        return new ResponseEntity(new ApiError(message), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        ApiError errors = new ApiError();
        for (FieldError fieldError : fieldErrors) {
            errors.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

}
