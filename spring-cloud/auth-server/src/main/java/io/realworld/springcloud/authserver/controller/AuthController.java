package io.realworld.springcloud.authserver.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.nimbusds.jose.jwk.RSAKey;
import io.realworld.springcloud.authserver.dto.RegisterRequestDto;
import io.realworld.springcloud.authserver.dto.UserDto;
import io.realworld.springcloud.authserver.entity.User;
import io.realworld.springcloud.authserver.mapper.UserMapper;
import io.realworld.springcloud.authserver.security.JwtUtils;
import io.realworld.springcloud.authserver.service.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final UserService userService;

  private final UserMapper userMapper;
  private final RSAKey rsaJwk;

  @GetMapping("/api/user")
  public UserDto me(HttpServletRequest request) {
    String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    User user = userService.findUserByEmail(email).get();
    String token = request.getHeader(AUTHORIZATION).substring("Token ".length());

    UserDto userDto = userMapper.entityToDto(user);
    userDto.setToken(token);

    return userDto;
  }

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

    UserDto userDto = userMapper.entityToDto(savedUser);
    userDto.setToken(token);

    return userDto;
  }
}
