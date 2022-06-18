package io.realworld.articleservice.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;

import com.nimbusds.jwt.JWTClaimsSet;
import io.realworld.api.dto.ProfileDto;
import io.realworld.articleservice.dto.AddCommentDto;
import io.realworld.articleservice.dto.ArticleDto;
import io.realworld.articleservice.dto.ArticleListDto;
import io.realworld.articleservice.dto.AuthorDto;
import io.realworld.articleservice.dto.CommentDto;
import io.realworld.articleservice.dto.CreateArticleDto;
import io.realworld.articleservice.dto.UpdateArticleDto;
import io.realworld.articleservice.entity.Article;
import io.realworld.articleservice.entity.Comment;
import io.realworld.articleservice.mapper.ArticleMapper;
import io.realworld.articleservice.mapper.CommentMapper;
import io.realworld.articleservice.repository.condition.ArticleSearchCondition;
import io.realworld.articleservice.service.ArticleService;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  private final CommentMapper commentMapper;

  private final ArticleService articleService;

  @PostMapping("/articles")
  public ArticleDto createArticle(@RequestBody CreateArticleDto body, HttpServletRequest request) {
    String username = getUsername();
    Article article = articleService.createArticle(body.getTitle(), body.getDescription(),
        body.getBody(), body.getTagList(), username);

    return makeResponse(article, username, request.getHeader(AUTHORIZATION));
  }

  @GetMapping("/articles")
  public ArticleListDto getArticlesBySearchCondition(
      ArticleSearchCondition condition,
      @RequestParam(name = "offset", defaultValue = "0") int offset,
      @RequestParam(name = "limit", defaultValue = "20") int limit,
      HttpServletRequest request
  ) {
    List<Article> articles = articleService.searchArticle(condition, offset, limit);

    List<ArticleDto> articleDtoList = articles.stream().map(
            (article -> makeResponse(article, article.getAuthor(), request.getHeader(AUTHORIZATION)))
        )
        .collect(Collectors.toList());

    ArticleListDto response = new ArticleListDto();
    response.setArticleList(articleDtoList);

    return response;
  }

  @GetMapping("/articles/{slug}")
  public ArticleDto getSingleArticle(@PathVariable String slug, HttpServletRequest request) {
    Article article = articleService.findArticleBySlug(slug).orElseThrow();

    return makeResponse(article, article.getAuthor(), request.getHeader(AUTHORIZATION));
  }

  @PutMapping("/articles/{slug}")
  public ArticleDto updateArticle(@PathVariable String slug, @RequestBody UpdateArticleDto body,
      HttpServletRequest request) {
    Article article = articleService.findArticleBySlug(slug).orElseThrow();

    Article updatedArticle = articleMapper.updateArticleDtoToEntity(body, article);

    return makeResponse(updatedArticle, getUsername(), request.getHeader(AUTHORIZATION));
  }

  @DeleteMapping("/articles/{slug}")
  public void deleteArticle(@PathVariable String slug) {
    articleService.deleteArticleBySlug(slug);
  }

  @PostMapping("/articles/{slug}/favorite")
  public ArticleDto favorite(@PathVariable String slug, HttpServletRequest request) {
    Article favoritedArticle = articleService.favoriteArticleBySlug(slug, getUsername());

    return makeResponse(favoritedArticle, favoritedArticle.getAuthor(),
        request.getHeader(AUTHORIZATION));
  }

  @DeleteMapping("/articles/{slug}/favorite")
  public ArticleDto unfavorite(@PathVariable String slug, HttpServletRequest request) {
    Article unfavoritedArticle = articleService.unfavoriteArticleBySlug(slug, getUsername());

    return makeResponse(unfavoritedArticle, unfavoritedArticle.getAuthor(),
        request.getHeader(AUTHORIZATION));
  }

  @PostMapping("/articles/{slug}/comments")
  public CommentDto addComment(@PathVariable String slug, @RequestBody AddCommentDto body,
      HttpServletRequest request) {
    Comment newComment = articleService.addComment(slug, body.getBody(), getUsername());

    return makeResponse(newComment, newComment.getAuthor(), request.getHeader(AUTHORIZATION));
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

  public ArticleDto makeResponse(Article article, String username, String token) {
    ArticleDto articleDto = articleMapper.entityToDto(article);

    ProfileDto profileDto = getProfile(username, token);
    articleDto.setAuthor(makeAuthorDto(profileDto));
    if (article.getFavoritedUsers().contains(username)) {
      articleDto.setFavorited(true);
    }

    return articleDto;
  }

  public CommentDto makeResponse(Comment comment, String username, String token) {
    CommentDto commentDto = commentMapper.entityToDto(comment);

    ProfileDto profileDto = getProfile(username, token);
    commentDto.setAuthor(makeAuthorDto(profileDto));

    return commentDto;
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
