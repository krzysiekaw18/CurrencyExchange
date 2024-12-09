package com.ks.currencyexchange.adapter.out.api.nbp;

import java.util.List;

public record ExchangeRateResponse(
        String table,
        String code,
        String currency,
        List<Rate> rates
) {

    public record Rate(
            String no,
            String effectiveDate,
            double mid) {

    }
}
