package com.backend.exception;

import lombok.experimental.StandardException;

@StandardException
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
