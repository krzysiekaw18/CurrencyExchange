package com.ks.currencyexchange.application.domain.model.user;

public record UserId(long value) {
    public static UserId of(long userId) {
        return new UserId(userId);
    }
}
