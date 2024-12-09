package com.ks.currencyexchange.adapter.in.api.exchange;

import com.ks.currencyexchange.application.domain.model.common.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Currency exchange data")
record CurrencyExchangeRequest(
        @NotNull(message = "AccountId is required.")
        @Schema(description = "Unique account identifier")
        Long accountId,

        @NotNull(message = "Amount is required.")
        @Positive(message = "Amount must be greater then zero.")
        @Schema(description = "Amount to convert")
        BigDecimal amount,

        @NotNull(message = "Source currency code is required.")
        @Schema(description = "Source currency code")
        Currency fromCurrency,

        @NotNull(message = "Target currency code is required.")
        @Schema(description = "Target currency code")
        Currency toCurrency
) {
}
