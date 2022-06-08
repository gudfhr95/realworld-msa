package io.realworld.springcloud.authserver.mapper;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import io.realworld.springcloud.authserver.dto.UpdateRequestDto;
import io.realworld.springcloud.authserver.dto.UserDto;
import io.realworld.springcloud.authserver.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mappings({
      @Mapping(target = "token", ignore = true)
  })
  UserDto entityToDto(User user);

  @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
  User updateRequestDtoToEntity(
      UpdateRequestDto updateRequestDto,
      @MappingTarget User user
  );
}
