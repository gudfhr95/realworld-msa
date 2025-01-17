package io.realworld.microservices.authservice.service;

import io.realworld.microservices.authservice.entity.Authority;
import io.realworld.microservices.authservice.entity.User;
import io.realworld.microservices.authservice.repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findUserByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email));
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public Optional<User> findUserByEmail(String email) {
    return userRepository.findUserByEmail(email);
  }

  public void addAuthority(Long userId, String authority) {
    userRepository.findById(userId)
        .ifPresent(user -> {
          Authority newRole = new Authority(user.getUserId(), authority);

          HashSet<Authority> newAuthorities = new HashSet<>();
          if (user.getAuthorities() != null) {
            newAuthorities.addAll(user.getAuthorities());
          }

          newAuthorities.add(newRole);
          user.setAuthorities(newAuthorities);
          save(user);
        });
  }

  public void removeAuthority(Long userId, String authority) {
    userRepository.findById(userId)
        .ifPresent(user -> {
          Authority targetRole = new Authority(user.getUserId(), authority);
          if (user.getAuthorities() != null && user.getAuthorities().contains(targetRole)) {
            Set<Authority> newAuthorities = user.getAuthorities()
                .stream()
                .filter(auth -> !auth.equals(targetRole))
                .collect(Collectors.toSet());

            user.setAuthorities(newAuthorities);
            save(user);
          }
        });
  }
}
