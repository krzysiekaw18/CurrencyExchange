package com.ks.currencyexchange.application.port;

import com.ks.currencyexchange.application.domain.model.account.Account;
import com.ks.currencyexchange.application.domain.model.account.AccountId;

import java.util.Optional;

public interface AccountStorage {

    AccountId createNewAccount(final Account account);

    void updateAccountBalances(final Account account);

    Optional<Account> getAccountDetailsByAccountId(final AccountId accountId);
}
