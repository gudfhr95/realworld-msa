package io.realworld.articleservice.repository;

import static io.realworld.articleservice.entity.QArticle.article;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.realworld.articleservice.entity.Article;
import io.realworld.articleservice.repository.condition.ArticleSearchCondition;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ArticleQueryRepository {

  private final JPAQueryFactory query;

  public ArticleQueryRepository(EntityManager em) {
    this.query = new JPAQueryFactory(em);
  }

  public List<Article> search(ArticleSearchCondition condition, int offset, int limit) {
    String tag = condition.getTag();
    String author = condition.getAuthor();
    String favorited = condition.getFavorited();

    return query.select(article)
        .from(article)
        .where(containsTag(tag), author(author), favoritedBy(favorited))
        .offset(offset)
        .limit(limit)
        .orderBy(article.createdAt.desc())
        .fetch();
  }

  private BooleanExpression containsTag(String tag) {
    if (!StringUtils.hasText(tag)) {
      return null;
    }

    return article.tagList.contains(tag);
  }

  private BooleanExpression author(String author) {
    if (!StringUtils.hasText(author)) {
      return null;
    }

    return article.author.eq(author);
  }

  private BooleanExpression favoritedBy(String favorited) {
    if (!StringUtils.hasText(favorited)) {
      return null;
    }

    return article.favoritedUsers.contains(favorited);
  }
}
