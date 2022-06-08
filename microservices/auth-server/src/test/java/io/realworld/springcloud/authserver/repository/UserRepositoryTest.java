package io.realworld.springcloud.authserver.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.realworld.springcloud.authserver.entity.Authority;
import io.realworld.springcloud.authserver.entity.User;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  User user;

  @BeforeEach
  void setup() {
    user = User.builder()
        .email("test@test.com")
        .password("password")
        .username("test")
        .build();

    user = userRepository.save(user);
  }

  @Test
  void findByIdShouldReturnUserIfExists() {
    User foundUser = userRepository.findById(user.getUserId()).get();

    assertThat(foundUser).isEqualTo(user);
  }

  @Test
  void findByIdShouldNotReturnUserIfDoesNotExists() {
    assertThat(userRepository.findById(2L)).isEmpty();
  }

  @Test
  void whenSavingWithExistingEmailShouldThrowAnError() {
    User newUser = User.builder()
        .email("test@test.com")
        .password("password")
        .username("test")
        .build();

    assertThatThrownBy(() -> userRepository.save(newUser)).isInstanceOf(
        DataIntegrityViolationException.class);
  }

  @Test
  void sameAuthorityShouldNotBeAdded() {
    Authority authority = new Authority(user.getUserId(), "ROLE_USER");
    user.setAuthorities(new HashSet<>(Arrays.asList(authority, authority)));

    userRepository.save(user);

    assertThat(user.getAuthorities().size()).isOne();
  }
}