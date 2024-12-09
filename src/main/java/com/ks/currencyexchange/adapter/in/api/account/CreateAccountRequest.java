package com.ks.currencyexchange.adapter.in.api.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Input data for account creating")
record CreateAccountRequest(
        @NotEmpty(message = "User's first name cannot be empty.")
        @Schema(description = "User first name")
        String firstName,

        @NotEmpty(message = "User's last name cannot be empty.")
        @Schema(description = "User last name")
        String lastName,

        @NotNull(message = "PLN balance is required.")
        @Positive(message = "PLN balance must be greater than 0.")
        @Digits(integer = 17, fraction = 2)
        @Schema(description = "Initial value for PLN balance")
        BigDecimal plnBalance,

        @PositiveOrZero(message = "USD balance cannot be less than 0.")
        @Digits(integer = 17, fraction = 2)
        @Schema(description = "Initial value for USD balance")
        BigDecimal usdBalance
) {
}
