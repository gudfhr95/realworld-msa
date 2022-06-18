package io.realworld.articleservice.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "comment_table")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Comment extends BaseTime {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long commentId;

  @Version
  private int version;

  private String body;

  private String author;

  @ManyToOne
  @JoinColumn(name = "article_id")
  private Article article;
}
