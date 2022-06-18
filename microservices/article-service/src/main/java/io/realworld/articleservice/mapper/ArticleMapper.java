package io.realworld.articleservice.mapper;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import io.realworld.articleservice.dto.ArticleDto;
import io.realworld.articleservice.dto.UpdateArticleDto;
import io.realworld.articleservice.entity.Article;
import java.util.Set;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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
  public static int countFavorites(Set<String> favoritedUsers) {
    return favoritedUsers.size();
  }

  @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
  @Mappings({
      @Mapping(source = "title", target = "slug", qualifiedByName = "makeSlug")
  })
  Article updateArticleDtoToEntity(UpdateArticleDto updateArticleDto,
      @MappingTarget Article article);

  @Named("makeSlug")
  public static String makeSlug(String title) {
    return title.toLowerCase().replace(' ', '-');
  }
}
