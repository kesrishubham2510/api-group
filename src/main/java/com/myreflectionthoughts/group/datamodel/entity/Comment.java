package com.myreflectionthoughts.group.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Data
@Entity
@Table(name = "comments", schema = "letschat")
public class Comment {

    @Id
    @UuidGenerator
    @Column(name = "comment_id")
    private String commentId;

    @Column(name ="comment_text")
    private String commentText;

    @Column(name="commented_at")
    private String commentedAt;

    @ManyToOne
    @JoinColumn(name = "post_id_fk")
    private Post post;

    @OneToMany(cascade =  CascadeType.REFRESH, mappedBy = "comment")
    private List<CommentLike> likes;

    @Column(name = "user_id")
    private String userId;
}
