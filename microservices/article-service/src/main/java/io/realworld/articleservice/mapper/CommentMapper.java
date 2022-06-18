package io.realworld.articleservice.mapper;

import io.realworld.articleservice.dto.CommentDto;
import io.realworld.articleservice.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CommentMapper {

  @Mappings({
      @Mapping(target = "author", ignore = true)
  })
  CommentDto entityToDto(Comment comment);
}
