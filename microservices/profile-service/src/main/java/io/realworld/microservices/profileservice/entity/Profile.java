package io.realworld.microservices.profileservice.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "profile_table")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Profile {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private Long userId;

  private String username;

  private String bio;

  private String image;

  @Builder.Default
  @ElementCollection
  private Set<Long> followers = new HashSet<>();

  public void follow(Long userId) {
    Set<Long> newFollowers = new HashSet<>(followers);
    newFollowers.add(userId);

    followers = newFollowers;
  }

  public void unfollow(Long userId) {
    Set<Long> newFollowers = new HashSet<>(followers);
    newFollowers.remove(userId);

    followers = newFollowers;
  }
}
