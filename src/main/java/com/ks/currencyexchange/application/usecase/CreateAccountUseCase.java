package com.ks.currencyexchange.application.usecase;

import com.ks.currencyexchange.application.domain.model.account.Account;
import com.ks.currencyexchange.application.domain.model.account.AccountId;
import com.ks.currencyexchange.application.domain.model.user.User;
import com.ks.currencyexchange.application.domain.model.user.UserId;
import com.ks.currencyexchange.application.port.AccountStorage;
import com.ks.currencyexchange.application.port.UserStorage;
import com.ks.currencyexchange.infrastructure.common.SnowflakeIdGenerator;
import com.ks.currencyexchange.infrastructure.usecase.UseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CreateAccountUseCase implements UseCase<CreateAccountUseCase.Input, CreateAccountUseCase.Output> {

    private final UserStorage userStorage;
    private final AccountStorage accountStorage;

    @Override
    @Transactional
    public Output execute(final Input input) {
        final UserId userId = userStorage.createNewUser(new User(input.firstName(),
                                                                 input.lastName()));
        final AccountId accountId =
                accountStorage.createNewAccount(new Account(AccountId.of(SnowflakeIdGenerator.getInstance().generate()),
                                                            userId,
                                                            input.plnBalance(),
                                                            initUsdBalance(input.usdBalance())));
        return new Output(accountId);
    }

    private BigDecimal initUsdBalance(final BigDecimal usdBalance) {
        return usdBalance == null
               ? BigDecimal.ZERO
               : usdBalance;
    }

    public record Input(String firstName,
                        String lastName,
                        BigDecimal plnBalance,
                        BigDecimal usdBalance
    ) implements UseCase.InputValues {
    }

    public record Output(AccountId accountId) implements UseCase.OutputValues {
    }
}
