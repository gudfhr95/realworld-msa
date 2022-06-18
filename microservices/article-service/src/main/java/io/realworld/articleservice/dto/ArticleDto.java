package io.realworld.articleservice.dto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@JsonTypeName("article")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Data
public class ArticleDto {

  public String slug;
  public String title;
  public String description;
  public String body;
  public String[] tagList;
  public String createdAt;
  public String updatedAt;
  public boolean favorited;
  public int favoritesCount;
  public AuthorDto author;
}
