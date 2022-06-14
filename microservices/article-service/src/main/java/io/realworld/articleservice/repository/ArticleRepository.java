package io.realworld.articleservice.repository;

import io.realworld.articleservice.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
