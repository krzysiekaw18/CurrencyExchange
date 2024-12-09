package com.ks.currencyexchange.application.domain.model.account;

import com.ks.currencyexchange.application.domain.model.user.UserId;

import java.math.BigDecimal;

public record Account(
        AccountId accountId,
        UserId userId,
        BigDecimal plnBalance,
        BigDecimal usdBalance
) {
}
