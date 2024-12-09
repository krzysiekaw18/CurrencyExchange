package com.ks.currencyexchange.adapter.in.api.account;

import com.ks.currencyexchange.application.domain.model.account.AccountId;
import com.ks.currencyexchange.application.usecase.CreateAccountUseCase;
import com.ks.currencyexchange.application.usecase.GetAccountDetailsUseCase;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
@Tag(name = "Account API's")
class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountDetailsUseCase getAccountDetailsUseCase;

    @Operation(summary = "Create new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created account details",
                         content = {
                                 @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                          schema = @Schema(implementation = CreateAccountRequest.class))
                         }),
            @ApiResponse(responseCode = "400", description = "Incorrect request params",
                         content = {
                                 @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                          schema = @Schema(implementation = ApiProblemDetail.class))
                         })
    })
    @PostMapping
    public CreateAccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        final CreateAccountUseCase.Output output =
                createAccountUseCase.execute(new CreateAccountUseCase.Input(request.firstName(),
                                                                            request.lastName(),
                                                                            request.plnBalance(),
                                                                            request.usdBalance()));

        return new CreateAccountResponse(output.accountId().value());
    }

    @Operation(summary = "Get account details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account details",
                         content = {
                                 @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                          schema = @Schema(implementation = AccountDetailsResponse.class))
                         }),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                         content = {
                                 @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                          schema = @Schema(implementation = ApiProblemDetail.class))
                         }),
    })
    @GetMapping(value = "/{id}")
    public AccountDetailsResponse getAccountDetails(@PathVariable Long id) {
        final GetAccountDetailsUseCase.Output accountDetails =
                getAccountDetailsUseCase.execute(new GetAccountDetailsUseCase.Input(AccountId.of(id)));
        return new AccountDetailsResponse(accountDetails.firstName(),
                                          accountDetails.lastName(),
                                          accountDetails.plnBalance(),
                                          accountDetails.usdBalance());
    }

}
