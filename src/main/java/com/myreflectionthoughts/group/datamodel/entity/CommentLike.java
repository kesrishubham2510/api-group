package com.myreflectionthoughts.group.datamodel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;


@Data
@Entity
@Table( name = "comment_like", schema = "letschat")
public class CommentLike {
    @Id
    @UuidGenerator
    @Column(name="like_id")
    private String likeId;

    @Column(name="user_id")
    private String userId;

    @Column(name = "liked_at")
    private String likedAt;

    @ManyToOne
    @JoinColumn(name = "comment_id_fk")
    private Comment comment;
}
