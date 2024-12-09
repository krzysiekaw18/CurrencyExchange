package com.ks.currencyexchange.application.usecase;

import com.ks.currencyexchange.application.domain.model.account.Account;
import com.ks.currencyexchange.application.domain.model.account.AccountId;
import com.ks.currencyexchange.application.domain.model.common.Currency;
import com.ks.currencyexchange.application.domain.model.user.UserId;
import com.ks.currencyexchange.application.exception.InvalidAmountException;
import com.ks.currencyexchange.application.exception.InvalidInputException;
import com.ks.currencyexchange.application.exception.ResourceNotFoundException;
import com.ks.currencyexchange.application.port.AccountStorage;
import com.ks.currencyexchange.application.port.CurrencyRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConvertAmountBetweenCurrenciesUseCaseTest {

    @Mock
    private CurrencyRateService currencyRateService;

    @Mock
    private AccountStorage accountStorage;

    private ConvertAmountBetweenCurrenciesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ConvertAmountBetweenCurrenciesUseCase(currencyRateService, accountStorage);
    }

    @Test
    void shouldThrowExceptionWhenBothCurrenciesAreTheSame() {
        //given
        //when
        final Throwable throwable =
                catchThrowable(() -> useCase.execute(new ConvertAmountBetweenCurrenciesUseCase.Input(
                        AccountId.of(1),
                        BigDecimal.valueOf(34.12),
                        Currency.PLN,
                        Currency.PLN)));

        //then
        assertThat(throwable).isInstanceOf(InvalidInputException.class)
                             .hasMessage("Both currencies are the same");
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        //given
        when(accountStorage.getAccountDetailsByAccountId(any())).thenReturn(Optional.empty());

        //when
        final Throwable throwable = catchThrowable(
                () -> useCase.execute(new ConvertAmountBetweenCurrenciesUseCase.Input(AccountId.of(333),
                                                                                      BigDecimal.valueOf(34.12),
                                                                                      Currency.PLN,
                                                                                      Currency.USD)));

        //then
        assertThat(throwable).isInstanceOf(ResourceNotFoundException.class)
                             .hasMessage("Not found account with id: 333");
    }

    @Test
    void shouldInvokeAccountDetailsByAccountIdWithCorrectParam() {
        //given
        when(accountStorage.getAccountDetailsByAccountId(any()))
                .thenReturn(Optional.of(new Account(AccountId.of(11),
                                                    UserId.of(2),
                                                    BigDecimal.valueOf(15.40),
                                                    BigDecimal.ONE)));
        when(currencyRateService.getRateForCurrency(any())).thenReturn(BigDecimal.valueOf(2));

        //when
        useCase.execute(new ConvertAmountBetweenCurrenciesUseCase.Input(AccountId.of(333),
                                                                        BigDecimal.valueOf(4.50),
                                                                        Currency.PLN,
                                                                        Currency.USD));

        //then
        verify(accountStorage).getAccountDetailsByAccountId(eq(AccountId.of(333)));
    }

    @Test
    void shouldCorrectUpdateBalanceWhenSourceCurrencyIsPLN() {
        //given
        final ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        when(accountStorage.getAccountDetailsByAccountId(any()))
                .thenReturn(Optional.of(new Account(AccountId.of(11),
                                                    UserId.of(2),
                                                    BigDecimal.valueOf(18.60),
                                                    BigDecimal.valueOf(12))));
        when(currencyRateService.getRateForCurrency(any())).thenReturn(BigDecimal.valueOf(2));

        //when
        useCase.execute(new ConvertAmountBetweenCurrenciesUseCase.Input(AccountId.of(11),
                                                                        BigDecimal.valueOf(8.60),
                                                                        Currency.PLN,
                                                                        Currency.USD));
        //then
        verify(accountStorage).updateAccountBalances(accountArgumentCaptor.capture());
        assertThat(accountArgumentCaptor.getValue()).usingRecursiveComparison()
                                                    .isEqualTo(new Account(AccountId.of(11),
                                                                           UserId.of(2),
                                                                           BigDecimal.valueOf(10.00),
                                                                           BigDecimal.valueOf(16.30)));
    }

    @Test
    void shouldCorrectUpdateBalanceWhenSourceCurrencyIsUSD() {
        //given
        final ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        when(accountStorage.getAccountDetailsByAccountId(any()))
                .thenReturn(Optional.of(new Account(AccountId.of(11),
                                                    UserId.of(2),
                                                    BigDecimal.valueOf(18.60),
                                                    BigDecimal.valueOf(12))));
        when(currencyRateService.getRateForCurrency(any())).thenReturn(BigDecimal.valueOf(2));

        //when
        useCase.execute(new ConvertAmountBetweenCurrenciesUseCase.Input(AccountId.of(11),
                                                                        BigDecimal.valueOf(8.60),
                                                                        Currency.USD,
                                                                        Currency.PLN));
        //then
        verify(accountStorage).updateAccountBalances(accountArgumentCaptor.capture());
        assertThat(accountArgumentCaptor.getValue()).usingRecursiveComparison()
                                                    .isEqualTo(new Account(AccountId.of(11),
                                                                           UserId.of(2),
                                                                           BigDecimal.valueOf(35.80),
                                                                           BigDecimal.valueOf(3.40)));
    }

    @Test
    void shouldThrowExceptionWhenAmountIsGreaterThanBalance() {
        //given
        when(accountStorage.getAccountDetailsByAccountId(any()))
                .thenReturn(Optional.of(new Account(AccountId.of(11),
                                                    UserId.of(2),
                                                    BigDecimal.valueOf(18.60),
                                                    BigDecimal.valueOf(12))));

        //when
        final Throwable throwable = catchThrowable(
                () -> useCase.execute(new ConvertAmountBetweenCurrenciesUseCase.Input(AccountId.of(11),
                                                                                      BigDecimal.valueOf(20.00),
                                                                                      Currency.PLN,
                                                                                      Currency.USD)));

        //then
        assertThat(throwable).isInstanceOf(InvalidAmountException.class)
                             .hasMessage("Amount to convert is greater than actual balance");
    }
}