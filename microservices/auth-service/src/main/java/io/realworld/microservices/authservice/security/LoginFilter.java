package io.realworld.microservices.authservice.security;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.RSAKey;
import io.realworld.microservices.authservice.dto.LoginRequestDto;
import io.realworld.microservices.authservice.dto.UserDto;
import io.realworld.microservices.authservice.entity.User;
import io.realworld.microservices.authservice.mapper.UserMapper;
import io.realworld.util.security.JwtUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private ObjectMapper objectMapper = new ObjectMapper();

  private final UserMapper userMapper;
  private final RSAKey rsaJWK;

  public LoginFilter(AuthenticationManager authenticationManager, UserMapper userMapper,
      RSAKey rsaJWK) {
    super(authenticationManager);

    this.userMapper = userMapper;
    this.rsaJWK = rsaJWK;

    setFilterProcessesUrl("/api/users/login");
  }

  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) {
    ServletInputStream inputStream = request.getInputStream();
    String messageBody = StreamUtils.copyToString(inputStream, UTF_8);
    LoginRequestDto loginRequestDto = objectMapper.readValue(messageBody, LoginRequestDto.class);

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        loginRequestDto.getEmail(), loginRequestDto.getPassword(), null);

    return getAuthenticationManager().authenticate(token);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authentication
  ) throws IOException {
    User user = (User) authentication.getPrincipal();

    String token;
    try {
      token = JwtUtils.generateToken(user.getEmail(), user.getUserId(), user.getUsername(), rsaJWK);
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }

    UserDto userDto = userMapper.entityToDto(user);
    userDto.setToken(token);

    response.setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE);
    response.getOutputStream().write(objectMapper.writeValueAsBytes(userDto));
  }
}
