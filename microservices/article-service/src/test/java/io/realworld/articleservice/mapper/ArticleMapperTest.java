package io.realworld.articleservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.realworld.articleservice.dto.ArticleDto;
import io.realworld.articleservice.entity.Article;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ArticleMapperTest {

  ArticleMapper articleMapper = Mappers.getMapper(ArticleMapper.class);

  @Test
  void entityToDtoTest() {
    Article article = Article.builder()
        .articleId(1L)
        .slug("slug")
        .title("title")
        .description("description")
        .body("body")
        .tagList(Collections.singleton("tag"))
        .favoritedUsers(Collections.singleton(1L))
        .build();

    ArticleDto articleDto = articleMapper.entityToDto(article);

    assertThat(articleDto.getSlug()).isEqualTo(article.getSlug());
    assertThat(articleDto.getTitle()).isEqualTo(article.getTitle());
    assertThat(articleDto.getDescription()).isEqualTo(article.getDescription());
    assertThat(articleDto.getBody()).isEqualTo(article.getBody());
    assertThat(articleDto.getTagList()).isEqualTo(article.getTagList().toArray());
    assertThat(articleDto.getFavoritesCount()).isOne();
  }
}