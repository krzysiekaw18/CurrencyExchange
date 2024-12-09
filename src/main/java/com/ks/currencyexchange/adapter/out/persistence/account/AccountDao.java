package com.ks.currencyexchange.adapter.out.persistence.account;

import com.ks.currencyexchange.application.domain.model.account.Account;
import com.ks.currencyexchange.application.domain.model.account.AccountId;
import com.ks.currencyexchange.application.domain.model.user.UserId;
import com.ks.currencyexchange.application.port.AccountStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
class AccountDao implements AccountStorage {

    private final AccountRepository accountRepository;

    @Override
    public AccountId createNewAccount(final Account account) {
        return AccountId.of(accountRepository.save(mapToAccountEntity(account))
                                             .getId());
    }

    @Override
    public void updateAccountBalances(Account account) {
        accountRepository.save(mapToAccountEntity(account));
    }

    @Override
    public Optional<Account> getAccountDetailsByAccountId(final AccountId accountId) {
        return accountRepository.findById(accountId.value())
                                .map(this::mapToAccount);
    }

    private AccountEntity mapToAccountEntity(final Account account) {
        return new AccountEntity(account.accountId().value(),
                                 account.userId().value(),
                                 account.plnBalance(),
                                 account.usdBalance());
    }

    private Account mapToAccount(final AccountEntity entity) {
        return new Account(AccountId.of(entity.getId()),
                           UserId.of(entity.getUserId()),
                           entity.getPlnBalance(),
                           entity.getUsdBalance());
    }
}
