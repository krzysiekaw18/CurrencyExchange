package com.ks.currencyexchange.application.domain.model.account;

public record AccountId(long value) {
    public static AccountId of(long accountId) {
        return new AccountId(accountId);
    }

    public String toString() {
        return Long.toString(value);
    }
}
