package io.realworld.springcloud.authserver.security;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.RSAKey;
import io.realworld.springcloud.authserver.dto.LoginRequestDto;
import io.realworld.springcloud.authserver.dto.LoginResponseDto;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private ObjectMapper objectMapper = new ObjectMapper();

  private final RSAKey rsaJWK;

  public LoginFilter(AuthenticationManager authenticationManager, RSAKey rsaJWK) {
    super(authenticationManager);

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
      token = JwtUtils.generateToken(user, rsaJWK);
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }

    LoginResponseDto loginResponseDto = new LoginResponseDto(user.getUsername(), token, null, null,
        null);

    response.setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE);
    response.getOutputStream().write(objectMapper.writeValueAsBytes(loginResponseDto));
  }
}
