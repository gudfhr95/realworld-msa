package io.realworld.articleservice.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import io.realworld.util.security.JwtUtils;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

  private final String jwkEndpoint;
  private final RestTemplate restTemplate;

  public JwtAuthenticationFilter(
      AuthenticationManager authenticationManager,
      String jwkEndpoint,
      RestTemplate restTemplate
  ) {
    super(authenticationManager);

    this.jwkEndpoint = jwkEndpoint;
    this.restTemplate = restTemplate;
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
    SignedJWT signedJWT = JwtUtils.verify(jwt, rsaJwk());

    if (signedJWT == null) {
      throw new AuthenticationException("Token is not valid");
    }

    UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
        signedJWT.getJWTClaimsSet(), null, null
    );
    SecurityContextHolder.getContext().setAuthentication(userToken);

    chain.doFilter(request, response);
  }

  @SneakyThrows
  public RSAKey rsaJwk() {
    String url = "http://" + jwkEndpoint + "/.well-known/jwks.json";
    ResponseEntity<String> response = restTemplate.exchange(url, GET, null, String.class);

    String jwkString = response.getBody();
    JWKSet jwkSet = JWKSet.parse(jwkString);

    return (RSAKey) jwkSet.getKeys().get(0);
  }
}