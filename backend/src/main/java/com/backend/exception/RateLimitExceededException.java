package com.backend.exception;

import lombok.experimental.StandardException;

@StandardException
public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}
