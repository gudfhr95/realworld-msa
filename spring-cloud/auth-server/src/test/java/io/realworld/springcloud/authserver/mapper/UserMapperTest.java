package io.realworld.springcloud.authserver.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.realworld.springcloud.authserver.dto.UpdateRequestDto;
import io.realworld.springcloud.authserver.dto.UserDto;
import io.realworld.springcloud.authserver.entity.User;
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
}