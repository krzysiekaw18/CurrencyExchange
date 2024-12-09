package com.ks.currencyexchange.application.usecase;

import com.ks.currencyexchange.application.domain.model.account.Account;
import com.ks.currencyexchange.application.domain.model.account.AccountId;
import com.ks.currencyexchange.application.domain.model.user.User;
import com.ks.currencyexchange.application.domain.model.user.UserId;
import com.ks.currencyexchange.application.port.AccountStorage;
import com.ks.currencyexchange.application.port.UserStorage;
import com.ks.currencyexchange.application.exception.ResourceNotFoundException;
import com.ks.currencyexchange.infrastructure.usecase.UseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class GetAccountDetailsUseCase
        implements UseCase<GetAccountDetailsUseCase.Input, GetAccountDetailsUseCase.Output> {

    private final UserStorage userStorage;
    private final AccountStorage accountStorage;

    @Override
    public Output execute(final Input input) {
        final Account account = getAccountFromStorage(input.accountId());
        final User user = getUserFromStorage(account.userId());

        return new Output(user.firstName(),
                          user.lastName(),
                          account.plnBalance(),
                          account.usdBalance());
    }

    private Account getAccountFromStorage(final AccountId accountId) {
        return accountStorage.getAccountDetailsByAccountId(accountId)
                             .orElseThrow(() -> new ResourceNotFoundException(
                                     String.format("Not found account with id: %s", accountId.value())));
    }

    private User getUserFromStorage(final UserId userId) {
        return userStorage.getUserDetailsByUserId(userId)
                          .orElseThrow(() -> new ResourceNotFoundException(
                                  String.format("Not found user with id: %s", userId.value())));
    }

    public record Input(AccountId accountId) implements UseCase.InputValues {
    }

    public record Output(String firstName,
                         String lastName,
                         BigDecimal plnBalance,
                         BigDecimal usdBalance) implements UseCase.OutputValues {
    }
}
