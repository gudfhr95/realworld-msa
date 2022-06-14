package io.realworld.articleservice.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

  @Column(unique = true, nullable = false)
  private String slug;

  @Column(nullable = false)
  private String title;

  private String description;

  private String body;

  @Builder.Default
  @ElementCollection
  private Set<String> tagList = new HashSet<>();

  @Builder.Default
  @ElementCollection
  private Set<Long> favoritedUsers = new HashSet<>();

  private String author;
}
