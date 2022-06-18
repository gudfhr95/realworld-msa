package io.realworld.microservices.profileservice.mapper;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import io.realworld.api.dto.ProfileDto;
import io.realworld.api.message.UserMessage;
import io.realworld.microservices.profileservice.entity.Profile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

  ProfileDto entityToDto(Profile profile);

  Profile userMessageToEntity(UserMessage userMessage);

  @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
  Profile userMessageToEntity(
      UserMessage userMessage,
      @MappingTarget Profile profile
  );
}
