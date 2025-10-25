package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

@Data
public class AddCommentToPostResponse{
    private String commentId;
    private String comment;
    private String userId;
    private String postId;
}
