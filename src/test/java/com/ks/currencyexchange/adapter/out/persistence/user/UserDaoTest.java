package com.ks.currencyexchange.adapter.out.persistence.user;

import com.ks.currencyexchange.application.domain.model.user.User;
import com.ks.currencyexchange.application.domain.model.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

    @Mock
    private UserRepository userRepository;

    private UserDao dao;

    @BeforeEach
    void setUp() {
        dao = new UserDao(userRepository);
    }

    @Test
    void shouldCorrectMapDomainToEntity() {
        //given
        final ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        when(userRepository.save(any())).thenReturn(UserEntity.builder()
                                                              .id(1L)
                                                              .build());

        //when
        dao.createNewUser(new User("Jan",
                                   "Kowalski"));

        //then
        verify(userRepository).save(userEntityArgumentCaptor.capture());
        assertThat(userEntityArgumentCaptor.getValue()).usingRecursiveComparison()
                                                       .isEqualTo(UserEntity.builder()
                                                                            .firstName("Jan")
                                                                            .lastName("Kowalski")
                                                                            .build());
    }

    @Test
    void shouldReturnUserId() {
        //given
        when(userRepository.save(any())).thenReturn(UserEntity.builder()
                                                              .id(1L)
                                                              .build());

        //when
        final UserId userId = dao.createNewUser(new User("Jan",
                                                         "Kowalski"));

        //then
        assertThat(userId).usingRecursiveComparison()
                          .isEqualTo(UserId.of(1));
    }

    @Test
    void shouldCorrectMapFromEntityToDomain() {
        //given
        when(userRepository.findById(any())).thenReturn(Optional.of(UserEntity.builder()
                                                                              .id(1L)
                                                                              .firstName("Jan")
                                                                              .lastName("Kowalski")
                                                                              .build()));

        //when
        final Optional<User> result = dao.getUserDetailsByUserId(UserId.of(1));

        //then
        assertThat(result.get()).usingRecursiveComparison()
                                .isEqualTo(new User("Jan",
                                                    "Kowalski"));
    }
}