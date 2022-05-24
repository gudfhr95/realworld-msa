package io.realworld.springcloud.authserver.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Table(name = "user_table")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long userId;

  private String email;

  private String password;

  private String username;

  private String bio;

  private String image;

  @OneToMany(fetch = EAGER, cascade = ALL)
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id"))
  private Set<Authority> authorities;

  private boolean enabled;

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return enabled;
  }

  @Override
  public boolean isAccountNonLocked() {
    return enabled;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return enabled;
  }
}
