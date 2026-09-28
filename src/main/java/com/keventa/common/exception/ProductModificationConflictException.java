package com.keventa.common.exception;

public class ProductModificationConflictException extends RuntimeException {
    public ProductModificationConflictException(String message) {
        super(message);
    }
}
