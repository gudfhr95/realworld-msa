package io.realworld.microservices.authservice.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.nimbusds.jose.jwk.RSAKey;
import io.realworld.microservices.authservice.dto.RegisterRequestDto;
import io.realworld.microservices.authservice.dto.UpdateRequestDto;
import io.realworld.microservices.authservice.dto.UserDto;
import io.realworld.microservices.authservice.entity.User;
import io.realworld.microservices.authservice.mapper.UserMapper;
import io.realworld.microservices.authservice.service.UserService;
import io.realworld.util.security.JwtUtils;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final UserService userService;

  private final UserMapper userMapper;
  private final RSAKey rsaJwk;

  @GetMapping("/user")
  public UserDto me(HttpServletRequest request) {
    String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    User user = userService.findUserByEmail(email).get();
    String token = request.getHeader(AUTHORIZATION).substring("Token ".length());

    UserDto userDto = userMapper.entityToDto(user);
    userDto.setToken(token);

    return userDto;
  }

  @SneakyThrows
  @PostMapping("/users")
  public UserDto register(@RequestBody RegisterRequestDto body) {
    User user = User.builder()
        .email(body.getEmail())
        .password(body.getPassword())
        .username(body.getUsername())
        .build();

    User savedUser = userService.save(user);
    userService.addAuthority(savedUser.getUserId(), "ROLE_USER");

    String token = JwtUtils.generateToken(user.getEmail(), user.getUserId(), rsaJwk);

    UserDto userDto = userMapper.entityToDto(savedUser);
    userDto.setToken(token);

    return userDto;
  }

  @SneakyThrows
  @PutMapping("/user")
  public UserDto update(@RequestBody UpdateRequestDto body) {
    String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    User user = userService.findUserByEmail(email).get();

    User updatedUser = userMapper.updateRequestDtoToEntity(body, user);
    updatedUser = userService.save(updatedUser);

    String token = JwtUtils.generateToken(updatedUser.getEmail(), updatedUser.getUserId(), rsaJwk);

    UserDto userDto = userMapper.entityToDto(updatedUser);
    userDto.setToken(token);

    return userDto;
  }
}
