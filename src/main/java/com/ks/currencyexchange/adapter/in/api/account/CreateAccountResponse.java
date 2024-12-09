package com.ks.currencyexchange.adapter.in.api.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Output details for created account")
record CreateAccountResponse(
        @NotNull
        @Schema(description = "Unique accountId")
        Long accountId
) {
}
