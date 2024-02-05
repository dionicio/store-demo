package com.store.demo.exception;

public class ItemRequiredException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ItemRequiredException(String message) {
        super(message);
    }
}