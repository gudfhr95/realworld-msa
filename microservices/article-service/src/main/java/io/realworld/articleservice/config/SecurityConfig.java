package io.realworld.articleservice.config;

import io.realworld.articleservice.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${app.auth-service}")
  private String jwkEndpoint;

  private final RestTemplate restTemplate;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
        authenticationManager(),
        jwkEndpoint,
        restTemplate
    );

    http.csrf().disable()
        .cors().disable()
        .formLogin().disable()
        .logout().disable()
        .authorizeRequests()
        .antMatchers("/actuator/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterAt(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
  }
}
