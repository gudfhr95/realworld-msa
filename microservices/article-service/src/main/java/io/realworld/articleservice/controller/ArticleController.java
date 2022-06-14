package io.realworld.articleservice.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;

import com.nimbusds.jwt.JWTClaimsSet;
import io.realworld.api.dto.ProfileDto;
import io.realworld.articleservice.dto.ArticleDto;
import io.realworld.articleservice.dto.AuthorDto;
import io.realworld.articleservice.dto.CreateArticleDto;
import io.realworld.articleservice.entity.Article;
import io.realworld.articleservice.mapper.ArticleMapper;
import io.realworld.articleservice.service.ArticleService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

  @Value("${app.profile-service}")
  String profileServiceUrl;

  private final RestTemplate restTemplate;

  private final ArticleMapper articleMapper;

  private final ArticleService articleService;

  @PostMapping("/articles")
  public ArticleDto createArticle(HttpServletRequest request, @RequestBody CreateArticleDto body) {
    String username = getUsername();
    Article article = articleService.createArticle(body.getTitle(), body.getDescription(),
        body.getBody(), body.getTagList(), username);

    ArticleDto articleDto = articleMapper.entityToDto(article);

    ProfileDto profileDto = getProfile(username, request.getHeader(AUTHORIZATION));
    articleDto.setAuthor(makeAuthorDto(profileDto));

    return articleDto;
  }

  private String getUsername() {
    JWTClaimsSet claimsSet = getJwtClaimsSet();

    return (String) claimsSet.getClaim("username");
  }

  private JWTClaimsSet getJwtClaimsSet() {
    JWTClaimsSet claimsSet = (JWTClaimsSet) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    return claimsSet;
  }

  private ProfileDto getProfile(String username, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(AUTHORIZATION, token);

    HttpEntity<String> entity = new HttpEntity<>("body", headers);

    ResponseEntity<ProfileDto> response = restTemplate.exchange(
        "http://" + profileServiceUrl + "/api/profiles/" + username, GET, entity, ProfileDto.class);

    return response.getBody();
  }

  private AuthorDto makeAuthorDto(ProfileDto profileDto) {
    return AuthorDto.builder()
        .username(profileDto.getUsername())
        .bio(profileDto.getBio())
        .image(profileDto.getImage())
        .following(profileDto.isFollowing())
        .build();
  }
}
