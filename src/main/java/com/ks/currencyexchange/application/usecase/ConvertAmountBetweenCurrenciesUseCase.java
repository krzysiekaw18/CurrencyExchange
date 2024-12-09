package com.ks.currencyexchange.application.usecase;

import com.ks.currencyexchange.application.domain.model.account.Account;
import com.ks.currencyexchange.application.domain.model.account.AccountId;
import com.ks.currencyexchange.application.domain.model.common.Currency;
import com.ks.currencyexchange.application.exception.InvalidAmountException;
import com.ks.currencyexchange.application.exception.InvalidInputException;
import com.ks.currencyexchange.application.exception.ResourceNotFoundException;
import com.ks.currencyexchange.application.port.AccountStorage;
import com.ks.currencyexchange.application.port.CurrencyRateService;
import com.ks.currencyexchange.infrastructure.usecase.ConsumerUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.ks.currencyexchange.application.domain.model.common.Currency.PLN;

@Service
@AllArgsConstructor
public class ConvertAmountBetweenCurrenciesUseCase implements
                                                   ConsumerUseCase<ConvertAmountBetweenCurrenciesUseCase.Input> {

    private final CurrencyRateService currencyRateService;
    private final AccountStorage accountStorage;

    @Override
    public void execute(final Input input) {
        validateInputCurrencies(input);

        final Account account = getAccountFromStorage(input.accountId());

        if (PLN.equals(input.fromCurrency())) {
            accountBalancesUpdate(account,
                                  account.plnBalance().subtract(input.amount()),
                                  account.usdBalance()
                                         .add(getAmountInUSD(input.toCurrency(), input.amount(), account)));
        } else {
            accountBalancesUpdate(account,
                                  account.plnBalance()
                                         .add(getAmountInPLN(input.fromCurrency(), input.amount(), account)),
                                  account.usdBalance().subtract(input.amount()));
        }
    }

    private void validateInputCurrencies(final Input input) {
        if (input.fromCurrency().equals(input.toCurrency())) {
            throw new InvalidInputException("Both currencies are the same");
        }
    }

    private Account getAccountFromStorage(final AccountId accountId) {
        return accountStorage.getAccountDetailsByAccountId(accountId)
                             .orElseThrow(() -> new ResourceNotFoundException(
                                     String.format("Not found account with id: %s", accountId.value())));
    }

    private void accountBalancesUpdate(final Account account,
                                       final BigDecimal plnBalance,
                                       final BigDecimal usdBalance) {
        accountStorage.updateAccountBalances(new Account(account.accountId(),
                                                         account.userId(),
                                                         plnBalance,
                                                         usdBalance));
    }

    private BigDecimal getAmountInUSD(final Currency toCurrency, final BigDecimal amount, final Account account) {
        return getActualBalance(amount, account.plnBalance())
                .divide(getRate(toCurrency), RoundingMode.HALF_UP);
    }

    private BigDecimal getAmountInPLN(final Currency fromCurrency, final BigDecimal amount, final Account account) {
        return getActualBalance(amount, account.usdBalance())
                .multiply(getRate(fromCurrency));
    }

    private BigDecimal getActualBalance(final BigDecimal amount, final BigDecimal accountBalance) {
        return Optional.of(amount)
                       .filter(a -> a.compareTo(accountBalance) <= 0)
                       .stream()
                       .findAny()
                       .orElseThrow(InvalidAmountException::new);
    }

    private BigDecimal getRate(final Currency currency) {
        return currencyRateService.getRateForCurrency(currency);
    }

    public record Input(
            AccountId accountId,
            BigDecimal amount,
            Currency fromCurrency,
            Currency toCurrency
    ) implements ConsumerUseCase.InputValues {
    }

}
