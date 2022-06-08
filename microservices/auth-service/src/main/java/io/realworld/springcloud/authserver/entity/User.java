package io.realworld.springcloud.authserver.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.userdetails.UserDetails;

@Table(name = "user_table")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long userId;

  @Version
  private int version;

  @Email
  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String username;

  private String bio;

  private String image;

  @OneToMany(fetch = EAGER, cascade = ALL)
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id"))
  private Set<Authority> authorities;

  @Builder.Default
  @ColumnDefault("true")
  private boolean enabled = true;

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
