package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

@Data
public class PostDTO {
    private String postId;
    private String content;
    private int likes;
    private String postedAt;
    private UserDetailsDTO author;
}
