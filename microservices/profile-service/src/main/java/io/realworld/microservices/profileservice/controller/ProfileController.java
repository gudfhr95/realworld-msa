package io.realworld.microservices.profileservice.controller;

import io.realworld.microservices.profileservice.dto.ProfileDto;
import io.realworld.microservices.profileservice.entity.Profile;
import io.realworld.microservices.profileservice.mapper.ProfileMapper;
import io.realworld.microservices.profileservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

  private final ProfileMapper profileMapper;

  private final ProfileService profileService;

  @GetMapping("/profiles/{username}")
  public ProfileDto getProfile(@PathVariable String username) {
    Profile foundProfile = profileService.findProfileByUsername(username).orElseThrow();

    ProfileDto profileDto = profileMapper.entityToDto(foundProfile);
    profileDto.setFollowing(false);

    if (isLoggedIn()) {
      Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      boolean isFollowing = foundProfile.getFollowers().contains(userId);

      profileDto.setFollowing(isFollowing);
    }

    return profileDto;
  }

  private boolean isLoggedIn() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Long;
  }
}
