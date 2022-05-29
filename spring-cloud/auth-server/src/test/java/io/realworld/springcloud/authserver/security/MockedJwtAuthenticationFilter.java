package io.realworld.springcloud.authserver.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class MockedJwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String VALID_TOKEN = "VALID_TOKEN";

  @SneakyThrows
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) {
    String token = request.getHeader(AUTHORIZATION);
    if (token == null || !token.startsWith("Token ")) {
      chain.doFilter(request, response);
      return;
    }

    String jwt = token.substring("Token ".length());

    if (jwt.equals(VALID_TOKEN)) {
      UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
          "test@test.com", null, null);
      SecurityContextHolder.getContext().setAuthentication(userToken);
    }

    chain.doFilter(request, response);
  }
}
