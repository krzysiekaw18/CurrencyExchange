package com.ks.currencyexchange.adapter.out.persistence.user;

import com.ks.currencyexchange.application.domain.model.user.User;
import com.ks.currencyexchange.application.domain.model.user.UserId;
import com.ks.currencyexchange.application.port.UserStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
class UserDao implements UserStorage {

    private final UserRepository userRepository;

    @Override
    public UserId createNewUser(final User user) {
        return UserId.of(userRepository.save(mapToUserEntity(user))
                                       .getId());
    }

    @Override
    public Optional<User> getUserDetailsByUserId(final UserId userId) {
        return userRepository.findById(userId.value())
                             .map(this::mapToUser);
    }

    private UserEntity mapToUserEntity(final User user) {
        return UserEntity.builder()
                         .firstName(user.firstName())
                         .lastName(user.lastName())
                         .build();
    }

    private User mapToUser(final UserEntity entity) {
        return new User(entity.getFirstName(),
                        entity.getLastName());
    }
}
