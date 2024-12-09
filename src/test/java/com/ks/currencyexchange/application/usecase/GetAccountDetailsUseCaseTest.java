package com.ks.currencyexchange.application.usecase;

import com.ks.currencyexchange.application.domain.model.account.Account;
import com.ks.currencyexchange.application.domain.model.account.AccountId;
import com.ks.currencyexchange.application.domain.model.user.User;
import com.ks.currencyexchange.application.domain.model.user.UserId;
import com.ks.currencyexchange.application.exception.ResourceNotFoundException;
import com.ks.currencyexchange.application.port.AccountStorage;
import com.ks.currencyexchange.application.port.UserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class GetAccountDetailsUseCaseTest {

    @Mock
    private UserStorage userStorage;

    @Mock
    private AccountStorage accountStorage;

    private GetAccountDetailsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAccountDetailsUseCase(userStorage, accountStorage);
    }

    @Test
    void shouldReturnCorrectAccountDetails() {
        //given
        when(accountStorage.getAccountDetailsByAccountId(any()))
                .thenReturn(Optional.of(new Account(AccountId.of(1),
                                                    UserId.of(2),
                                                    BigDecimal.valueOf(34.56),
                                                    BigDecimal.ZERO)));
        when(userStorage.getUserDetailsByUserId(any())).thenReturn(Optional.of(new User("Jan",
                                                                                        "Nowak")));

        //when
        final GetAccountDetailsUseCase.Output result =
                useCase.execute(new GetAccountDetailsUseCase.Input(new AccountId(9)));

        //then
        assertThat(result).usingRecursiveComparison()
                          .isEqualTo(new GetAccountDetailsUseCase.Output("Jan",
                                                                         "Nowak",
                                                                         BigDecimal.valueOf(34.56),
                                                                         BigDecimal.ZERO));
    }

    @Test
    void shouldThrowExceptionWhenNotFoundAccount() {
        //given
        when(accountStorage.getAccountDetailsByAccountId(any())).thenReturn(Optional.empty());

        //when
        final Throwable throwable =
                catchThrowable(() -> useCase.execute(new GetAccountDetailsUseCase.Input(new AccountId(9))));

        //then
        assertThat(throwable).isInstanceOf(ResourceNotFoundException.class)
                             .hasMessage("Not found account with id: 9");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        //given
        when(accountStorage.getAccountDetailsByAccountId(any()))
                .thenReturn(Optional.of(new Account(AccountId.of(1),
                                                    UserId.of(2),
                                                    BigDecimal.valueOf(34.56),
                                                    BigDecimal.ZERO)));
        when(userStorage.getUserDetailsByUserId(any())).thenReturn(Optional.empty());

        //when
        final Throwable throwable =
                catchThrowable(() -> useCase.execute(new GetAccountDetailsUseCase.Input(new AccountId(9))));

        //then
        assertThat(throwable).isInstanceOf(ResourceNotFoundException.class)
                             .hasMessage("Not found user with id: 2");
    }

    @Test
    void shouldInvokeGetAccountDetailsByAccountIdWithCorrectParams() {
        //given
        when(accountStorage.getAccountDetailsByAccountId(any()))
                .thenReturn(Optional.of(new Account(AccountId.of(1),
                                                    UserId.of(2),
                                                    BigDecimal.valueOf(34.56),
                                                    BigDecimal.ZERO)));
        when(userStorage.getUserDetailsByUserId(any())).thenReturn(Optional.of(new User("Jan",
                                                                                        "Nowak")));

        //when
        useCase.execute(new GetAccountDetailsUseCase.Input(new AccountId(9)));

        //then
        verify(accountStorage).getAccountDetailsByAccountId(eq(AccountId.of(9)));
    }

    @Test
    void shouldInvokeGetUserDetailsByUserIdWithCorrectParams() {
        //given
        when(accountStorage.getAccountDetailsByAccountId(any()))
                .thenReturn(Optional.of(new Account(AccountId.of(1),
                                                    UserId.of(2),
                                                    BigDecimal.valueOf(34.56),
                                                    BigDecimal.ZERO)));
        when(userStorage.getUserDetailsByUserId(any())).thenReturn(Optional.of(new User("Jan",
                                                                                        "Nowak")));

        //when
        useCase.execute(new GetAccountDetailsUseCase.Input(new AccountId(9)));

        //then
        verify(userStorage).getUserDetailsByUserId(eq(UserId.of(2)));
    }
}