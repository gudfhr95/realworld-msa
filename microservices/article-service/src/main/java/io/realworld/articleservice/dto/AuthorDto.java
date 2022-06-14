package io.realworld.articleservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDto {

  public String username;
  public String bio;
  public String image;
  public boolean following;
}
