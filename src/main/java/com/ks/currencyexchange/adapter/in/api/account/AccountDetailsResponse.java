package com.ks.currencyexchange.adapter.in.api.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Account details")
record AccountDetailsResponse(
        @NotNull
        @Schema(description = "User first name")
        String firstName,

        @NotNull
        @Schema(description = "User last name")
        String lastName,

        @NotNull
        @Schema(description = "Account PLN balance")
        BigDecimal plnBalance,

        @Schema(description = "Account USD balance")
        BigDecimal usdBalance
) {
}
