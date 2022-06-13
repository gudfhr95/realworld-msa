package io.realworld.microservices.profileservice.controller;

import static io.realworld.microservices.profileservice.security.MockedJwtAuthenticationFilter.USER_ID;
import static io.realworld.microservices.profileservice.security.MockedJwtAuthenticationFilter.VALID_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.realworld.microservices.profileservice.dto.FollowRequestDto;
import io.realworld.microservices.profileservice.entity.Profile;
import io.realworld.microservices.profileservice.mapper.ProfileMapper;
import io.realworld.microservices.profileservice.mapper.ProfileMapperImpl;
import io.realworld.microservices.profileservice.security.MockedJwtAuthenticationFilter;
import io.realworld.microservices.profileservice.service.ProfileService;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(ProfileController.class)
@ComponentScan(basePackageClasses = {ProfileMapper.class, ProfileMapperImpl.class})
class ProfileControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  WebApplicationContext webApplicationContext;

  @MockBean
  ProfileService profileService;

  @Autowired
  ProfileMapper profileMapper;
  @Autowired
  ObjectMapper objectMapper;

  Profile profile;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .addFilter(new MockedJwtAuthenticationFilter())
        .build();

    profile = Profile.builder()
        .userId(123L)
        .username("username")
        .bio("bio")
        .image("image")
        .build();
  }

  @Test
  void whenGetProfileExpectProfile() throws Exception {
    when(profileService.findProfileByUsername(any())).thenReturn(Optional.of(profile));

    mockMvc.perform(
            get("/api/profiles/username").contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + VALID_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.profile.username").value("username"))
        .andExpect(jsonPath("$.profile.bio").value("bio"))
        .andExpect(jsonPath("$.profile.image").value("image"))
        .andExpect(jsonPath("$.profile.following").value(false));
  }

  @Test
  void whenFollowProfileExpectProfileWithFollowingTrue() throws Exception {
    profile.setFollowers(Collections.singleton(USER_ID));
    when(profileService.follow(any(), any())).thenReturn(profile);

    mockMvc.perform(
            post("/api/profiles/username/follow").contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + VALID_TOKEN)
                .content(objectMapper.writeValueAsString(followRequestDto())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.profile.username").value("username"))
        .andExpect(jsonPath("$.profile.bio").value("bio"))
        .andExpect(jsonPath("$.profile.image").value("image"))
        .andExpect(jsonPath("$.profile.following").value(true));
  }

  @Test
  void whenUnfollowProfileExpectProfileWithFollowingFalse() throws Exception {
    when(profileService.unfollow(any(), any())).thenReturn(profile);

    mockMvc.perform(
            delete("/api/profiles/username/follow").contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, "Token " + VALID_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.profile.username").value("username"))
        .andExpect(jsonPath("$.profile.bio").value("bio"))
        .andExpect(jsonPath("$.profile.image").value("image"))
        .andExpect(jsonPath("$.profile.following").value(false));
  }

  private FollowRequestDto followRequestDto() {
    FollowRequestDto dto = new FollowRequestDto();

    dto.setEmail("test@test.com");

    return dto;
  }
}