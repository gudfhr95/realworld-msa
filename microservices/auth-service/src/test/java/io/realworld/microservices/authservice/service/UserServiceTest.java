package io.realworld.microservices.authservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import io.realworld.microservices.authservice.entity.User;
import io.realworld.microservices.authservice.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private static final Long EXISTING_USER_ID = 1L;
  private static final String EXISTING_EMAIL = "existing@email.com";
  private static final String NOT_EXISTING_EMAIL = "not_exising@email.com";

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService userService;

  User user;

  @BeforeEach
  void setup() {
    user = new User();
  }

  @Test
  void whenLoadUserByExistingEmailShouldReturnUser() {
    when(userRepository.findUserByEmail(EXISTING_EMAIL)).thenReturn(Optional.of(user));

    User foundUser = (User) userService.loadUserByUsername(EXISTING_EMAIL);

    assertThat(foundUser).isEqualTo(user);
  }

  @Test
  void whenLoadUserByNotExistingEmailShouldThrowAnException() {
    when(userRepository.findUserByEmail(NOT_EXISTING_EMAIL)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> {
      userService.loadUserByUsername(NOT_EXISTING_EMAIL);
    }).isInstanceOf(UsernameNotFoundException.class);
  }

  @Test
  void whenAddSameAuthorityShouldNotContainDuplicatedAuthority() {
    when(userRepository.findById(EXISTING_USER_ID)).thenReturn(Optional.of(user));

    userService.addAuthority(EXISTING_USER_ID, "ROLE_USER");
    userService.addAuthority(EXISTING_USER_ID, "ROLE_USER");

    assertThat(user.getAuthorities().size()).isOne();
  }
}