package com.blog.aigetcode.exceptions;

import java.io.Serializable;
import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {
    private final Class clazz;
    private final Serializable id;

    public NotFoundException(Serializable id, Class clazz) {
        this(MessageFormat.format("Entity ''{0}'' #{1} was not found.", clazz, id), id, clazz);
    }

    public NotFoundException(String message, Serializable id, Class clazz) {
        super(message, null);
        this.id = id;
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    public Serializable getId() {
        return id;
    }
}
