package io.realworld.springcloud.authserver.controller;

import static io.realworld.springcloud.authserver.security.MockedJwtAuthenticationFilter.VALID_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.realworld.springcloud.authserver.dto.RegisterRequestDto;
import io.realworld.springcloud.authserver.dto.UpdateRequestDto;
import io.realworld.springcloud.authserver.entity.User;
import io.realworld.springcloud.authserver.mapper.UserMapper;
import io.realworld.springcloud.authserver.mapper.UserMapperImpl;
import io.realworld.springcloud.authserver.security.MockedJwtAuthenticationFilter;
import io.realworld.springcloud.authserver.service.UserService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(AuthController.class)
@ComponentScan(basePackageClasses = {UserMapper.class, UserMapperImpl.class})
@Slf4j
class AuthControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  WebApplicationContext webApplicationContext;

  @MockBean
  UserService userService;
  @Autowired
  UserMapper userMapper;
  @Autowired
  ObjectMapper objectMapper;

  User user;

  @BeforeEach
  public void setupWebApplicationContext() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .addFilter(new MockedJwtAuthenticationFilter())
        .build();

    user = User.builder()
        .email("test@test.com")
        .username("username")
        .bio("bio")
        .image("image")
        .build();
  }


  @Test
  void whenGetUserExpectCurrentUser() throws Exception {
    when(userService.findUserByEmail(any())).thenReturn(Optional.of(user));

    mockMvc.perform(
            get("/api/user").contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + VALID_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user.email").value("test@test.com"))
        .andExpect(jsonPath("$.user.token").value(VALID_TOKEN))
        .andExpect(jsonPath("$.user.username").value("username"))
        .andExpect(jsonPath("$.user.bio").value("bio"))
        .andExpect(jsonPath("$.user.image").value("image"));
  }

  @Test
  void whenPostUsersExpectNewUser() throws Exception {
    when(userService.save(any())).thenReturn(user);

    mockMvc.perform(
            post("/api/users").contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequestDto())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user.email").value("test@test.com"))
        .andExpect(jsonPath("$.user.token").exists())
        .andExpect(jsonPath("$.user.username").value("username"))
        .andExpect(jsonPath("$.user.bio").value("bio"))
        .andExpect(jsonPath("$.user.image").value("image"));
  }

  @Test
  void whenPutUserExpectUpdatedUser() throws Exception {
    when(userService.findUserByEmail(any())).thenReturn(Optional.of(user));
    when(userService.save(any())).thenReturn(user);

    mockMvc.perform(
            put("/api/user").contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + VALID_TOKEN)
                .content(objectMapper.writeValueAsString(updateRequestDto())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user.email").value("test@test.com"))
        .andExpect(jsonPath("$.user.token").exists())
        .andExpect(jsonPath("$.user.username").value("newUsername"))
        .andExpect(jsonPath("$.user.bio").value("newBio"))
        .andExpect(jsonPath("$.user.image").value("newImage"));
    ;
  }

  private RegisterRequestDto registerRequestDto() {
    RegisterRequestDto dto = new RegisterRequestDto();

    dto.setEmail("test@test.com");
    dto.setPassword("password");
    dto.setUsername("username");

    return dto;
  }

  private UpdateRequestDto updateRequestDto() {
    UpdateRequestDto dto = new UpdateRequestDto();

    dto.setUsername("newUsername");
    dto.setBio("newBio");
    dto.setImage("newImage");

    return dto;
  }
}
