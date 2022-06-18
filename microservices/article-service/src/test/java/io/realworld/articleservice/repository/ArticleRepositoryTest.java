package io.realworld.articleservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.realworld.articleservice.entity.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ArticleRepositoryTest {

  @Autowired
  ArticleRepository articleRepository;

  Article article;

  @BeforeEach
  void setup() {
    article = Article.builder()
        .slug("slug")
        .build();

    article = articleRepository.save(article);
  }

  @Test
  void findBySlugShouldReturnArticleIfExists() {
    Article foundArticle = articleRepository.findBySlug(article.getSlug()).get();

    assertThat(foundArticle).isEqualTo(article);
  }

  @Test
  void findBySlugShouldNotReturnArticleIfDoesNotExists() {
    assertThat(articleRepository.findBySlug("slug-that-does-not-exist")).isEmpty();
  }
}