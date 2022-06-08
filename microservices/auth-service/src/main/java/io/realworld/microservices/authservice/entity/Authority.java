package io.realworld.microservices.authservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Table(name = "user_authority_table")
@Entity
@IdClass(Authority.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Authority implements GrantedAuthority {

  @Id
  @Column(name = "user_id")
  private Long userId;

  @Id
  private String authority;
}
