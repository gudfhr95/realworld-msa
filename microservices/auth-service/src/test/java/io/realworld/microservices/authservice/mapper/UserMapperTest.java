package io.realworld.microservices.authservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.realworld.api.message.UserMessage;
import io.realworld.microservices.authservice.dto.UpdateRequestDto;
import io.realworld.microservices.authservice.dto.UserDto;
import io.realworld.microservices.authservice.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserMapperTest {

  UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Test
  void entityToDtoTest() {
    User user = User.builder()
        .email("test@test.com")
        .password("password")
        .username("username")
        .bio("bio")
        .image("image")
        .build();

    UserDto userDto = userMapper.entityToDto(user);

    assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
    assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
    assertThat(userDto.getBio()).isEqualTo(user.getBio());
    assertThat(userDto.getImage()).isEqualTo(user.getImage());
    assertThat(userDto.getToken()).isNull();
  }

  @Test
  void updateRequestToDtoTest() {
    UpdateRequestDto updateRequestDto = new UpdateRequestDto();
    updateRequestDto.setEmail("test@test.com");
    updateRequestDto.setPassword("password");
    updateRequestDto.setUsername("username");
    updateRequestDto.setBio("bio");
    updateRequestDto.setImage("image");

    User user = new User();
    user = userMapper.updateRequestDtoToEntity(updateRequestDto, user);

    assertThat(user.getEmail()).isEqualTo(updateRequestDto.getEmail());
    assertThat(user.getPassword()).isEqualTo(updateRequestDto.getPassword());
    assertThat(user.getUsername()).isEqualTo(updateRequestDto.getUsername());
    assertThat(user.getBio()).isEqualTo(updateRequestDto.getBio());
    assertThat(user.getImage()).isEqualTo(updateRequestDto.getImage());
  }

  @Test
  void whenUpdateRequestDtoHasNullValueShouldIgnore() {
    UpdateRequestDto updateRequestDto = new UpdateRequestDto();
    updateRequestDto.setEmail("test@test.com");
    updateRequestDto.setUsername("username");
    updateRequestDto.setBio("bio");
    updateRequestDto.setImage("image");

    User user = new User();
    user = userMapper.updateRequestDtoToEntity(updateRequestDto, user);

    assertThat(user.getEmail()).isEqualTo(updateRequestDto.getEmail());
    assertThat(user.getPassword()).isNull();
    assertThat(user.getUsername()).isEqualTo(updateRequestDto.getUsername());
    assertThat(user.getBio()).isEqualTo(updateRequestDto.getBio());
    assertThat(user.getImage()).isEqualTo(updateRequestDto.getImage());
  }

  @Test
  void entityToUserMessageTest() {
    User user = User.builder()
        .userId(1L)
        .email("test@test.com")
        .password("password")
        .username("username")
        .bio("bio")
        .image("image")
        .build();

    UserMessage userMessage = userMapper.entityToUserMessage(user);

    assertThat(userMessage.getUserId()).isEqualTo(user.getUserId());
    assertThat(userMessage.getUsername()).isEqualTo(user.getUsername());
    assertThat(userMessage.getBio()).isEqualTo(user.getBio());
    assertThat(userMessage.getImage()).isEqualTo(user.getImage());
  }
}