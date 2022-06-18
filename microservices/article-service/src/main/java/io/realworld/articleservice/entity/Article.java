package io.realworld.articleservice.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "article_table")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Article extends BaseTime {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long articleId;

  @Version
  private int version;

  @Column(unique = true)
  private String slug;

  private String title;

  private String description;

  private String body;

  @Builder.Default
  @ElementCollection
  private Set<String> tagList = new HashSet<>();

  @Builder.Default
  @ElementCollection
  private Set<String> favoritedUsers = new HashSet<>();

  private String author;

  @OneToMany(mappedBy = "article", cascade = ALL)
  private List<Comment> comments = new ArrayList<>();

  public void addFavoritedUser(String username) {
    Set<String> newFavoritedUsers = new HashSet<>(favoritedUsers);
    newFavoritedUsers.add(username);

    favoritedUsers = newFavoritedUsers;
  }

  public void removeFavoritedUser(String username) {
    Set<String> newFavoritedUsers = new HashSet<>(favoritedUsers);
    newFavoritedUsers.remove(username);

    favoritedUsers = newFavoritedUsers;
  }

  public void addComment(Comment comment) {
    comments.add(comment);
    comment.setArticle(this);
  }

  public void removeComment(Comment comment) {
    comments.remove(comment);
  }
}
