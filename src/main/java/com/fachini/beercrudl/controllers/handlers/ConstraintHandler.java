package com.fachini.beercrudl.controllers.handlers;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class ConstraintHandler {

    public static final String PREFIX = "constraint.violation";

    private Class<?> clazz;

    private ConstraintHandler() {
        // Utility Class
    }

    public static ConstraintHandler with(Class<?> clazz) {
        return new ConstraintHandler(clazz);
    }

    public String getKey(String message) {
        final Field[] declaredFields = clazz.getDeclaredFields();
        final Optional<Field> field = Arrays.stream(declaredFields)//
                .filter(f -> message.toLowerCase().contains(f.getName().toLowerCase()))//
                .findFirst();
        return field.map(value -> PREFIX + "." + value.getName()).orElse(PREFIX);
    }

}
