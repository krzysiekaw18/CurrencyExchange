package com.ks.currencyexchange.application.port;

import com.ks.currencyexchange.application.domain.model.common.Currency;

import java.math.BigDecimal;

public interface CurrencyRateService {
    BigDecimal getRateForCurrency(final Currency currency);
}
