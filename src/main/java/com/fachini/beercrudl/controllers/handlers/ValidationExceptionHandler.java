package com.fachini.beercrudl.controllers.handlers;

import static com.fachini.beercrudl.config.DefaultMessageSourceConfig.DEFAULT_LOCALE;

import java.util.Map;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fachini.beercrudl.entities.EntityConstraints;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
@Component
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String FIELD_INVALID_FORMAT = "field.invalid.format";

    private final MessageSource messageSource;

    public ValidationExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String key = ConstraintHandler.with(EntityConstraints.class).getKey(exception.getMostSpecificCause().getLocalizedMessage());
        final String message = messageSource.getMessage(key, null, DEFAULT_LOCALE);
        return new ResponseEntity(new ApiError(message), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<ApiError> handleRollbackException(RollbackException exception) {
        if (exception.getCause() instanceof ConstraintViolationException) {
            final ConstraintViolationException cause = (ConstraintViolationException) exception.getCause();
            return handleConstraintViolationException(cause);
        } else {
            return new ResponseEntity(new ApiError(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException exception) {
        final ApiError errors = new ApiError();
        final JsonMappingException.Reference reference = exception.getPath().get(0);
        final String message = messageSource.getMessage(FIELD_INVALID_FORMAT, null, DEFAULT_LOCALE);
        final String helper = getFieldHelper(reference.getFrom().getClass().getName(), reference.getFieldName());
        errors.addError(reference.getFieldName(), message, helper);
        return new ResponseEntity(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException exception) {
        final ApiError errors = new ApiError();
        exception.getConstraintViolations().forEach(constraintViolation -> {
            final String field = constraintViolation.getPropertyPath().toString();
            final String message = constraintViolation.getMessage();
            String customMessage = replaceAttributes(getMessage(message, field), constraintViolation);
            String helper = getFieldHelper(constraintViolation.getLeafBean().getClass().getName(), field);
            errors.addError(field, customMessage, helper);
        });
        return new ResponseEntity(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private String getMessage(String message, String field) {
        final String key = message + "." + field;
        String customMessage = messageSource.getMessage(key, null, DEFAULT_LOCALE);
        if (customMessage.equals(key)) {
            customMessage = messageSource.getMessage(message, null, DEFAULT_LOCALE);
        }
        return customMessage;
    }

    private String getFieldHelper(String message, String field) {
        final String key = message + "." + field + ".helper";
        final String customMessage = messageSource.getMessage(key, null, DEFAULT_LOCALE);
        if (customMessage.equals(key)) {
            return null;
        }
        return customMessage;
    }

    private String replaceAttributes(String message, ConstraintViolation constraintViolation) {
        String newMessage = message;
        final Map<String, Object> attributes = constraintViolation.getConstraintDescriptor().getAttributes();
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            newMessage = newMessage.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return newMessage;
    }

}
