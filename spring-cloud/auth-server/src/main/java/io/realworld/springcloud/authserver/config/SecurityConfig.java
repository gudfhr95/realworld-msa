package io.realworld.springcloud.authserver.config;

import com.nimbusds.jose.jwk.RSAKey;
import io.realworld.springcloud.authserver.jose.Jwks;
import io.realworld.springcloud.authserver.mapper.UserMapper;
import io.realworld.springcloud.authserver.security.JwtAuthenticationFilter;
import io.realworld.springcloud.authserver.security.LoginFilter;
import io.realworld.springcloud.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService userService;

  private final UserMapper userMapper;

  @Override
  protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder.userDetailsService(userService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    LoginFilter loginFilter = new LoginFilter(authenticationManager(), userMapper, rsaJWK());
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
        authenticationManager(), rsaJWK());

    http.csrf().disable()
        .cors().disable()
        .formLogin().disable()
        .logout().disable()
        .authorizeRequests()
        .antMatchers("/actuator/**").permitAll()
        .antMatchers("/.well-knwon/jwks.json").permitAll()
        .antMatchers("/api/users").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAt(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
  }

  @Bean
  public RSAKey rsaJWK() {
    try {
      return Jwks.generateRsa();
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }
}
