package com.blog.aigetcode.exceptions;

public interface Check {

    static <T> void required(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    static void required(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    static <T, E> void exist(T entity, Long id, Class<E> clazz) {
        if (entity == null) {
            throw new NotFoundException(id, clazz);
        }
    }

    static void fail(String message) {
        throw new InvalidException(message);
    }
}