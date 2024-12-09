package com.ks.currencyexchange.application.port;

import com.ks.currencyexchange.application.domain.model.user.User;
import com.ks.currencyexchange.application.domain.model.user.UserId;

import java.util.Optional;

public interface UserStorage {

    UserId createNewUser(final User user);

    Optional<User> getUserDetailsByUserId(final UserId userId);
}
