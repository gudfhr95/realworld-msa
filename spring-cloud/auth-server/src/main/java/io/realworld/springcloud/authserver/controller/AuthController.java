package io.realworld.springcloud.authserver.controller;

import com.nimbusds.jose.jwk.RSAKey;
import io.realworld.springcloud.authserver.dto.RegisterRequestDto;
import io.realworld.springcloud.authserver.dto.UserDto;
import io.realworld.springcloud.authserver.entity.User;
import io.realworld.springcloud.authserver.security.JwtUtils;
import io.realworld.springcloud.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final UserService userService;
  private final RSAKey rsaJwk;

  @SneakyThrows
  @PostMapping("/api/users")
  public UserDto register(@RequestBody RegisterRequestDto body) {
    User user = User.builder()
        .email(body.getEmail())
        .password(body.getPassword())
        .username(body.getUsername())
        .build();

    User savedUser = userService.save(user);
    userService.addAuthority(savedUser.getUserId(), "ROLE_USER");

    String token = JwtUtils.generateToken(savedUser, rsaJwk);

    return UserDto.builder()
        .email(savedUser.getEmail())
        .token(token)
        .username(savedUser.getRealUsername())
        .bio(savedUser.getBio())
        .image(savedUser.getImage())
        .build();
  }
}
