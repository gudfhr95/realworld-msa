package io.realworld.articleservice.service;

import io.realworld.articleservice.entity.Article;
import io.realworld.articleservice.repository.ArticleRepository;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final RestTemplate restTemplate;

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

  private String makeSlug(String title) {
    return title.toLowerCase().replace(' ', '-');
  }
}
