package io.realworld.articleservice.dto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import lombok.Data;

@JsonTypeName("articles")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@Data
public class ArticleListDto {

  List<ArticleDto> articleList;
}
