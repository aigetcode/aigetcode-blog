package com.blog.aigetcode.exceptions;

public class InvalidException extends RuntimeException {

    public InvalidException(String message) {
        this(message, null /* cause */);
    }

    public InvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
