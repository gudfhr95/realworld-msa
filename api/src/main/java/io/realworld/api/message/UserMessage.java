package io.realworld.api.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMessage {

  public static final String CREATE = "create-user";
  public static final String UPDATE = "update-user";

  public Long userId;
  public String username;
  public String bio;
  public String image;
}
