package com.fachini.beercrudl.controllers.handlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();

    private final List<Error> messages = new ArrayList<>(0);

    public ApiError(String message) {
        addError(message);
    }

    public void addError(String message) {
        addError(null, message);
    }

    public void addError(String field, String message) {
        addError(field, message, null);
    }

    public void addError(String field, String message, String helper) {
        addError(new Error(field, message, helper));
    }

    public void addError(Error error) {
        messages.add(error);
    }

    @Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Error {

        private final String field;
        private final String message;
        private final String helper;

    }

}
