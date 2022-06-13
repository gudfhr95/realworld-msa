package io.realworld.microservices.profileservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.realworld.api.message.UserMessage;
import io.realworld.microservices.profileservice.dto.ProfileDto;
import io.realworld.microservices.profileservice.entity.Profile;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ProfileMapperTest {

  ProfileMapper profileMapper = Mappers.getMapper(ProfileMapper.class);

  @Test
  void entityToDtoTest() {
    Profile profile = Profile.builder()
        .userId(1L)
        .username("username")
        .bio("bio")
        .image("image")
        .build();

    ProfileDto profileDto = profileMapper.entityToDto(profile);

    assertThat(profileDto.getUsername()).isEqualTo(profile.getUsername());
    assertThat(profileDto.getBio()).isEqualTo(profile.getBio());
    assertThat(profileDto.getImage()).isEqualTo(profile.getImage());
  }

  @Test
  void userMessageToEntityTest() {
    UserMessage userMessage = UserMessage.builder()
        .userId(1L)
        .username("username")
        .bio("bio")
        .image("image")
        .build();

    Profile profile = profileMapper.userMessageToEntity(userMessage);

    assertThat(profile.getUserId()).isEqualTo(userMessage.getUserId());
    assertThat(profile.getUsername()).isEqualTo(userMessage.getUsername());
    assertThat(profile.getBio()).isEqualTo(userMessage.getBio());
    assertThat(profile.getImage()).isEqualTo(userMessage.getImage());
    assertThat(profile.getFollowers().size()).isZero();
  }

  @Test
  void whenUserMessageHasNullValueShouldIgnore() {
    UserMessage userMessage = UserMessage.builder()
        .userId(1L)
        .username("username")
        .image("image")
        .build();

    Profile profile = new Profile();
    profile = profileMapper.userMessageToEntity(userMessage, profile);

    assertThat(profile.getUserId()).isEqualTo(userMessage.getUserId());
    assertThat(profile.getUsername()).isEqualTo(userMessage.getUsername());
    assertThat(profile.getBio()).isNull();
    assertThat(profile.getImage()).isEqualTo(userMessage.getImage());
  }
}