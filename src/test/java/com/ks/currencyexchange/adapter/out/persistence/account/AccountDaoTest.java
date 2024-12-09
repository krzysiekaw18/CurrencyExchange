package com.ks.currencyexchange.adapter.out.persistence.account;

import com.ks.currencyexchange.application.domain.model.account.Account;
import com.ks.currencyexchange.application.domain.model.account.AccountId;
import com.ks.currencyexchange.application.domain.model.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountDaoTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountDao dao;

    @BeforeEach
    void setUp() {
        dao = new AccountDao(accountRepository);
    }

    @Test
    void shouldCorrectMapFromDomainToEntity() {
        //given
        final ArgumentCaptor<AccountEntity> accountEntityArgumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        when(accountRepository.save(any())).thenReturn(AccountId.of(1));

        //when
        dao.createNewAccount(new Account(AccountId.of(33),
                                         UserId.of(44),
                                         BigDecimal.ONE,
                                         BigDecimal.TEN));

        //then
        verify(accountRepository).save(accountEntityArgumentCaptor.capture());
        assertThat(accountEntityArgumentCaptor.getValue()).usingRecursiveComparison()
                                                          .isEqualTo(AccountEntity.builder()
                                                                                  .id(33L)
                                                                                  .userId(44L)
                                                                                  .plnBalance(BigDecimal.ONE)
                                                                                  .usdBalance(BigDecimal.TEN)
                                                                                  .build());
    }

    @Test
    void shouldCorrectMapFromEntityToDomain() {
        //given
        when(accountRepository.findById(any())).thenReturn(Optional.of(AccountEntity.builder()
                                                                                    .id(4L)
                                                                                    .userId(7L)
                                                                                    .plnBalance(BigDecimal.ONE)
                                                                                    .usdBalance(BigDecimal.TEN)
                                                                                    .build()));

        //when
        final Optional<Account> result = dao.getAccountDetailsByAccountId(AccountId.of(4));

        //then
        assertThat(result.get()).usingRecursiveComparison()
                                .isEqualTo(new Account(AccountId.of(4),
                                                       UserId.of(7L),
                                                       BigDecimal.ONE,
                                                       BigDecimal.TEN));
    }
}