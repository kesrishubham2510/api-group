package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

@Data
public class PostDTO {
    private String postId;
    private String content;
    private int likes;
    private String postedAt;
    private UserDetailsDTO author;

    public PostDTO(){}

    public PostDTO(String postId, String content, int likes, String postedAt, UserDetailsDTO author) {
        this.postId = postId;
        this.content = content;
        this.likes = likes;
        this.postedAt = postedAt;
        this.author = author;
    }
}
