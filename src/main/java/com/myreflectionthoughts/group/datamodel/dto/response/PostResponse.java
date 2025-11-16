package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PostResponse {
    private String postId;
    private String content;
    private int likes;
    private boolean haveILiked;
    private String postedAt;
    private UserDetailsResponse author;

    public PostResponse(){}

    public PostResponse(String postId, String content, int likes, String postedAt, UserDetailsResponse author) {
        this.postId = postId;
        this.content = content;
        this.likes = likes;
        this.postedAt = postedAt;
        this.author = author;
    }
}
