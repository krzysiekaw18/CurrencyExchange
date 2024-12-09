package com.ks.currencyexchange.application.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Amount to convert is greater than actual balance");
    }
}
