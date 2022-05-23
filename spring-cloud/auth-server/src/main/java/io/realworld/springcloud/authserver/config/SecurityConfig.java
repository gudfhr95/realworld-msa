package io.realworld.springcloud.authserver.config;

import io.realworld.springcloud.authserver.jose.Jwks;
import io.realworld.springcloud.authserver.security.LoginFilter;
import java.security.KeyPair;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    LoginFilter loginFilter = new LoginFilter(authenticationManager(), keyPair());

    http.csrf().disable()
        .cors().disable()
        .formLogin().disable()
        .logout().disable()
        .authorizeRequests()
        .antMatchers("/actuator/**").permitAll()
        .antMatchers("/.well-knwon/jwks.json").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  public KeyPair keyPair() {
    try {
      return Jwks.generateRsa().toKeyPair();
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Bean
  UserDetailsService users() {
    UserDetails user = User.withDefaultPasswordEncoder()
        .username("test@test.com")
        .password("qwer1234")
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(user);
  }
}
