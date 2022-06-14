package io.realworld.articleservice.repository;

import io.realworld.articleservice.entity.Article;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  Optional<Article> findBySlug(String slug);
}
