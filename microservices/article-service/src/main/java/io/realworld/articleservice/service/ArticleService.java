package io.realworld.articleservice.service;

import io.realworld.articleservice.entity.Article;
import io.realworld.articleservice.entity.Comment;
import io.realworld.articleservice.repository.ArticleQueryRepository;
import io.realworld.articleservice.repository.ArticleRepository;
import io.realworld.articleservice.repository.CommentRepository;
import io.realworld.articleservice.repository.condition.ArticleSearchCondition;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final ArticleQueryRepository articleQueryRepository;

  private final CommentRepository commentRepository;

  public List<Article> searchArticle(ArticleSearchCondition condition, int offset, int limit) {
    return articleQueryRepository.search(condition, offset, limit);
  }

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

  public Article favoriteArticleBySlug(String slug, String username) {
    Article article = findArticleBySlug(slug).orElseThrow();

    article.addFavoritedUser(username);

    return article;
  }

  public Article unfavoriteArticleBySlug(String slug, String username) {
    Article article = findArticleBySlug(slug).orElseThrow();

    article.removeFavoritedUser(username);

    return article;
  }

  public Optional<Article> findArticleBySlug(String slug) {
    return articleRepository.findBySlug(slug);
  }

  public Comment addComment(String slug, String body, String author) {
    Article article = findArticleBySlug(slug).orElseThrow();

    Comment comment = Comment.builder()
        .body(body)
        .author(author)
        .article(article)
        .build();

    commentRepository.save(comment);

    article.addComment(comment);

    return comment;
  }

  private String makeSlug(String title) {
    return title.toLowerCase().replace(' ', '-');
  }
}
