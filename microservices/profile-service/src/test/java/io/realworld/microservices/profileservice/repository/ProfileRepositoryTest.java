package io.realworld.microservices.profileservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.realworld.microservices.profileservice.entity.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProfileRepositoryTest {

  @Autowired
  ProfileRepository profileRepository;

  Profile profile;

  @BeforeEach
  void setup() {
    profile = Profile.builder()
        .username("username")
        .bio("bio")
        .image("image")
        .build();

    profile = profileRepository.save(profile);
  }

  @Test
  void whenUsernameMatchesItShouldReturnProfile() {
    Profile foundProfile = profileRepository.findProfileByUsername(profile.getUsername()).get();

    assertThat(foundProfile).isEqualTo(profile);
  }

  @Test
  void whenUsernameDoesNotMatchItShouldReturnNothing() {
    assertThat(profileRepository.findProfileByUsername("usernameThatDoesNotExists")).isEmpty();
  }

  @Test
  void whenFollowItShouldContainUserIdInFollowerList() {
    profile.follow(123L);
    profileRepository.save(profile);

    Profile foundProfile = profileRepository.findProfileByUsername(profile.getUsername()).get();

    assertThat(foundProfile.getFollowers().size()).isOne();
    assertThat(foundProfile.getFollowers()).contains(123L);
  }

  @Test
  void whenUnFollowItShouldNotContainUserIdInFollowerList() {
    profile.follow(123L);
    profileRepository.save(profile);

    Profile foundProfile = profileRepository.findProfileByUsername(profile.getUsername()).get();
    foundProfile.unfollow(123L);
    profileRepository.save(profile);

    foundProfile = profileRepository.findProfileByUsername(profile.getUsername()).get();

    assertThat(foundProfile.getFollowers().size()).isZero();
  }
}