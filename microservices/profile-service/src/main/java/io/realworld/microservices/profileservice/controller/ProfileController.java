package io.realworld.microservices.profileservice.controller;

import io.realworld.api.dto.ProfileDto;
import io.realworld.microservices.profileservice.dto.FollowRequestDto;
import io.realworld.microservices.profileservice.entity.Profile;
import io.realworld.microservices.profileservice.mapper.ProfileMapper;
import io.realworld.microservices.profileservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    return buildProfileDto(foundProfile);
  }

  @PostMapping("/profiles/{username}/follow")
  public ProfileDto follow(@PathVariable String username, @RequestBody FollowRequestDto body) {
    Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Profile followedProfile = profileService.follow(username, userId);

    return buildProfileDto(followedProfile);
  }

  @DeleteMapping("/profiles/{username}/follow")
  public ProfileDto unfollow(@PathVariable String username) {
    Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Profile unfollowedProfile = profileService.unfollow(username, userId);

    return buildProfileDto(unfollowedProfile);
  }

  private ProfileDto buildProfileDto(Profile profile) {

    ProfileDto profileDto = profileMapper.entityToDto(profile);
    profileDto.setFollowing(false);

    if (isLoggedIn()) {
      Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      boolean isFollowing = profile.getFollowers().contains(userId);

      profileDto.setFollowing(isFollowing);
    }

    return profileDto;
  }

  private boolean isLoggedIn() {
    return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Long;
  }
}
