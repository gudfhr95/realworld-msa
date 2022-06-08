package io.realworld.microservices.authservice.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

  private final RSAKey rsaJWK;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager, RSAKey rsaJWK) {
    super(authenticationManager);

    this.rsaJWK = rsaJWK;
  }

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
    SignedJWT signedJWT = JwtUtils.verify(jwt, rsaJWK);

    if (signedJWT == null) {
      throw new AuthenticationException("Token is not valid");
    }

    UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
        signedJWT.getJWTClaimsSet().getSubject(), null, null);
    SecurityContextHolder.getContext().setAuthentication(userToken);

    chain.doFilter(request, response);
  }
}
