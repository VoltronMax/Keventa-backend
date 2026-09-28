package com.keventa.common.exception;

public class SaleAlreadyCancelledException extends RuntimeException {
    public SaleAlreadyCancelledException(String message) {
        super(message);
    }
}
