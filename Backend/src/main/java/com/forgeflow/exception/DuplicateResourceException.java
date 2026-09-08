package com.forgeflow.exception;

public class DuplicateResourceException extends RuntimeException {

    // Constructor receives the error message
    public DuplicateResourceException(String message) {
        super(message);
    }
}