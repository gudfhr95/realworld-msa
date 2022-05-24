package io.realworld.springcloud.authserver.mapper;

import io.realworld.springcloud.authserver.dto.UserDto;
import io.realworld.springcloud.authserver.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mappings({
      @Mapping(target = "token", ignore = true)
  })
  UserDto entityToDto(User user);
}
