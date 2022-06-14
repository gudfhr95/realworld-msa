package io.realworld.articleservice.mapper;

import io.realworld.articleservice.dto.ArticleDto;
import io.realworld.articleservice.entity.Article;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

  @Mappings({
      @Mapping(source = "favoritedUsers", target = "favoritesCount", qualifiedByName = "favoritesCount"),
      @Mapping(target = "author", ignore = true)
  })
  ArticleDto entityToDto(Article article);

  @Named("favoritesCount")
  public static int countFavorites(Set<Long> favoritedUsers) {
    return favoritedUsers.size();
  }
}
