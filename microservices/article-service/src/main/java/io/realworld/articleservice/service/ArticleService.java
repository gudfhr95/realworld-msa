package io.realworld.articleservice.service;

import io.realworld.articleservice.entity.Article;
import io.realworld.articleservice.repository.ArticleRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;

  public Article createArticle(String title, String description, String body, String[] tagList,
      String author) {
    Article article = Article.builder()
        .slug(makeSlug(title))
        .title(title)
        .description(description)
        .body(body)
        .tagList(new HashSet<>(List.of(tagList)))
        .author(author)
        .build();

    articleRepository.save(article);

    return article;
  }

  public void deleteArticleBySlug(String slug) {
    Article article = findArticleBySlug(slug).orElseThrow();

    articleRepository.delete(article);
  }

  public Optional<Article> findArticleBySlug(String slug) {
    return articleRepository.findBySlug(slug);
  }

  private String makeSlug(String title) {
    return title.toLowerCase().replace(' ', '-');
  }
}
