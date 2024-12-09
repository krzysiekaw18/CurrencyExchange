package com.ks.currencyexchange.application.usecase;

import com.ks.currencyexchange.application.domain.model.account.Account;
import com.ks.currencyexchange.application.domain.model.account.AccountId;
import com.ks.currencyexchange.application.domain.model.user.User;
import com.ks.currencyexchange.application.domain.model.user.UserId;
import com.ks.currencyexchange.application.port.AccountStorage;
import com.ks.currencyexchange.application.port.UserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Mock
    private UserStorage userStorage;

    @Mock
    private AccountStorage accountStorage;

    private CreateAccountUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateAccountUseCase(userStorage, accountStorage);
    }

    @Test
    void shouldInvokeCreateNewUserWithCorrectParams() {
        //given
        final ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userStorage.createNewUser(any())).thenReturn(UserId.of(1));
        when(accountStorage.createNewAccount(any())).thenReturn(AccountId.of(2));

        //when
        useCase.execute(new CreateAccountUseCase.Input("Jan",
                                                       "Kowalski",
                                                       BigDecimal.valueOf(2.43),
                                                       BigDecimal.valueOf(6.33)));

        //then
        verify(userStorage).createNewUser(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue()).usingRecursiveComparison()
                                                 .isEqualTo(new User("Jan",
                                                                     "Kowalski"));
    }

    @Test
    void shouldInvokeCreateNewAccountWithCorrectParamsWhenUsdBalanceNotNull() {
        //given
        final ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        when(userStorage.createNewUser(any())).thenReturn(UserId.of(1));
        when(accountStorage.createNewAccount(any())).thenReturn(AccountId.of(2));

        //when
        useCase.execute(new CreateAccountUseCase.Input("Jan",
                                                       "Kowalski",
                                                       BigDecimal.valueOf(2.43),
                                                       BigDecimal.valueOf(6.33)));

        //then
        verify(accountStorage).createNewAccount(accountArgumentCaptor.capture());
        assertThat(accountArgumentCaptor.getValue()).usingRecursiveComparison()
                                                    .ignoringFields("accountId")
                                                    .isEqualTo(new Account(AccountId.of(3),
                                                                           UserId.of(1),
                                                                           BigDecimal.valueOf(2.43),
                                                                           BigDecimal.valueOf(6.33)));
    }

    @Test
    void shouldInvokeCreateNewAccountWithCorrectParamsWhenUsdNull() {
        //given
        final ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        when(userStorage.createNewUser(any())).thenReturn(UserId.of(1));
        when(accountStorage.createNewAccount(any())).thenReturn(AccountId.of(2));

        //when
        useCase.execute(new CreateAccountUseCase.Input("Jan",
                                                       "Kowalski",
                                                       BigDecimal.valueOf(2.43),
                                                       null));

        //then
        verify(accountStorage).createNewAccount(accountArgumentCaptor.capture());
        assertThat(accountArgumentCaptor.getValue()).usingRecursiveComparison()
                                                    .ignoringFields("accountId")
                                                    .isEqualTo(new Account(AccountId.of(3),
                                                                           UserId.of(1),
                                                                           BigDecimal.valueOf(2.43),
                                                                           BigDecimal.ZERO));
    }

    @Test
    void shouldReturnAccountId() {
        //given
        final ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        when(userStorage.createNewUser(any())).thenReturn(UserId.of(1));
        when(accountStorage.createNewAccount(any())).thenReturn(AccountId.of(2));

        //when
        final CreateAccountUseCase.Output result = useCase.execute(
                new CreateAccountUseCase.Input("Jan",
                                               "Kowalski",
                                               BigDecimal.valueOf(2.43),
                                               null));

        //then
        assertThat(result).usingRecursiveComparison()
                          .isEqualTo(new CreateAccountUseCase.Output(AccountId.of(2)));
    }
}