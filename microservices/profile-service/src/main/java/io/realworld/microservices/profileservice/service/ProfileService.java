package io.realworld.microservices.profileservice.service;

import io.realworld.microservices.profileservice.entity.Profile;
import io.realworld.microservices.profileservice.repository.ProfileRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final ProfileRepository profileRepository;

  public Optional<Profile> findProfileByUsername(String username) {
    return profileRepository.findProfileByUsername(username);
  }

  public Profile follow(String username, Long userId) {
    Profile profile = profileRepository.findProfileByUsername(username).orElseThrow();

    profile.follow(userId);
    profileRepository.save(profile);

    return profile;
  }

  public Profile unfollow(String username, Long userId) {
    Profile profile = profileRepository.findProfileByUsername(username).orElseThrow();

    profile.unfollow(userId);
    profileRepository.save(profile);

    return profile;
  }
}
