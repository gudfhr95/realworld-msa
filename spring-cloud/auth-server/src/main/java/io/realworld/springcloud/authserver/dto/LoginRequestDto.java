package io.realworld.springcloud.authserver.dto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Value;

@JsonTypeName("user")
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@NoArgsConstructor(force = true)
@Value
public class LoginRequestDto {

  @Email
  public String email;

  @NotNull
  public String password;
}
