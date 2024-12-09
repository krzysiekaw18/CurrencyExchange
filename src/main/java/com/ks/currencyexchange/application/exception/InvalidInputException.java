package com.ks.currencyexchange.application.exception;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(final String message) {
        super(message);
    }
}
