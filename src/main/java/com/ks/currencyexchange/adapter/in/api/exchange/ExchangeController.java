package com.ks.currencyexchange.adapter.in.api.exchange;

import com.ks.currencyexchange.application.domain.model.account.AccountId;
import com.ks.currencyexchange.application.usecase.ConvertAmountBetweenCurrenciesUseCase;
import com.ks.currencyexchange.infrastructure.web.ApiProblemDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange")
@AllArgsConstructor
@Tag(name = "Currency exchange API's")
class ExchangeController {

    private final ConvertAmountBetweenCurrenciesUseCase convertAmountBetweenCurrenciesUseCase;

    @Operation(summary = "Convert amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "Balances updated"),
            @ApiResponse(responseCode = "400", description = "Incorrect request params",
                         content = {
                                 @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                          schema = @Schema(implementation = ApiProblemDetail.class))
                         }),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                         content = {
                                 @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                          schema = @Schema(implementation = ApiProblemDetail.class))
                         }),
            @ApiResponse(responseCode = "422", description = "Incorrect request params",
                         content = {
                                 @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                          schema = @Schema(implementation = ApiProblemDetail.class))
                         })
    })
    @PostMapping(value = "/convert")
    public void convertAmount(@Valid @RequestBody CurrencyExchangeRequest request) {
        convertAmountBetweenCurrenciesUseCase.execute(new ConvertAmountBetweenCurrenciesUseCase.Input(
                AccountId.of(request.accountId()),
                request.amount(),
                request.fromCurrency(),
                request.toCurrency()));
    }

}
