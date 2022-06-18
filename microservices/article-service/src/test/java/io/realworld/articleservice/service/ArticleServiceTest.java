package io.realworld.articleservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.realworld.articleservice.entity.Article;
import io.realworld.articleservice.repository.ArticleRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

  private static final String EXISTING_ARTICLE_SLUG = "existing-article-slug";
  private static final String NOT_EXISTING_ARTICLE_SLUG = "not-existing-article-slug";

  @Mock
  ArticleRepository articleRepository;

  @InjectMocks
  ArticleService articleService;

  Article article;

  @BeforeEach
  void setup() {
    article = new Article();
  }

  @Test
  void whenCreateArticleShouldReturnNewArticle() {
    Article newArticle = articleService.createArticle("title", "description", "body",
        new String[]{"tag"}, "author");

    assertThat(newArticle.getTitle()).isEqualTo("title");
    assertThat(newArticle.getDescription()).isEqualTo("description");
    assertThat(newArticle.getBody()).isEqualTo("body");
    assertThat(newArticle.getTagList()).contains("tag");
    assertThat(newArticle.getAuthor()).isEqualTo("author");
  }

  @Test
  void whenFindArticleByExistingSlugShouldReturnArticle() {
    when(articleRepository.findBySlug(EXISTING_ARTICLE_SLUG)).thenReturn(Optional.of(article));

    Article foundArticle = articleService.findArticleBySlug(EXISTING_ARTICLE_SLUG).get();

    assertThat(foundArticle).isEqualTo(article);
  }

  @Test
  void whenFindArticleByNotExistingSLugShouldReturnEmpty() {
    when(articleRepository.findBySlug(NOT_EXISTING_ARTICLE_SLUG)).thenReturn(Optional.empty());

    assertThat(articleService.findArticleBySlug(NOT_EXISTING_ARTICLE_SLUG)).isEmpty();
  }
}