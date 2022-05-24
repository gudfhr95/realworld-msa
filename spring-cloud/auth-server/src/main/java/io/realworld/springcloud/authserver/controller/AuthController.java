package io.realworld.springcloud.authserver.controller;

import io.realworld.springcloud.authserver.dto.RegisterRequestDto;
import io.realworld.springcloud.authserver.entity.User;
import io.realworld.springcloud.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final UserService userService;

  @PostMapping("/api/users")
  public void register(@RequestBody RegisterRequestDto body) {
    User user = User.builder()
        .email(body.getEmail())
        .password(body.getPassword())
        .username(body.getUsername())
        .build();

    User savedUser = userService.save(user);
    userService.addAuthority(savedUser.getUserId(), "ROLE_USER");
  }
}
