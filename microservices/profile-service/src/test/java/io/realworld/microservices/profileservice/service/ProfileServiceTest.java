package io.realworld.microservices.profileservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import io.realworld.microservices.profileservice.entity.Profile;
import io.realworld.microservices.profileservice.repository.ProfileRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

  private static final String EXISTING_USERNAME = "existingUsername";
  private static final String NOT_EXISTING_USERNAME = "notExistingUsername";

  @Mock
  ProfileRepository profileRepository;

  @InjectMocks
  ProfileService profileService;

  Profile profile;

  @BeforeEach
  void setup() {
    profile = new Profile();
  }

  @Test
  void whenFindUsernameWithExistingUsernameShouldReturnUser() {
    when(profileRepository.findProfileByUsername(EXISTING_USERNAME)).thenReturn(
        Optional.of(profile));

    Profile foundProfile = profileService.findProfileByUsername(EXISTING_USERNAME).get();

    assertThat(foundProfile).isEqualTo(profile);
  }

  @Test
  void whenFindUsernameWtihNotExistingUsernameShouldReturnNothing() {
    when(profileRepository.findProfileByUsername(NOT_EXISTING_USERNAME)).thenReturn(
        Optional.empty());

    assertThat(profileService.findProfileByUsername(NOT_EXISTING_USERNAME)).isEmpty();
  }

  @Test
  void whenFollowWithExistingUsernameShouldContainFollowerId() {
    when(profileRepository.findProfileByUsername(EXISTING_USERNAME)).thenReturn(
        Optional.of(profile));

    Profile followedProfile = profileService.follow(EXISTING_USERNAME, 123L);

    assertThat(followedProfile.getFollowers().size()).isOne();
    assertThat(followedProfile.getFollowers()).contains(123L);
  }

  @Test
  void whenFollowWithNotExistingUsernameShouldThrowAnError() {
    when(profileRepository.findProfileByUsername(NOT_EXISTING_USERNAME)).thenReturn(
        Optional.empty());

    assertThatThrownBy(() -> profileService.follow(NOT_EXISTING_USERNAME, 123L))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void whenUnfollowWithExistingUsernameShouldNotContainFollowerId() {
    profile.follow(123L);
    when(profileRepository.findProfileByUsername(EXISTING_USERNAME)).thenReturn(
        Optional.of(profile));

    Profile followedProfile = profileService.unfollow(EXISTING_USERNAME, 123L);

    assertThat(followedProfile.getFollowers().size()).isZero();
  }

  @Test
  void whenUnfollowWithExistingUsernameShouldThrowAnError() {
    when(profileRepository.findProfileByUsername(NOT_EXISTING_USERNAME)).thenReturn(
        Optional.empty());

    assertThatThrownBy(() -> profileService.unfollow(NOT_EXISTING_USERNAME, 123L))
        .isInstanceOf(NoSuchElementException.class);
  }
}