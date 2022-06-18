package io.realworld.articleservice.repository.condition;

import lombok.Data;

@Data
public class ArticleSearchCondition {

  private String tag;
  private String author;
  private String favorited;
}
