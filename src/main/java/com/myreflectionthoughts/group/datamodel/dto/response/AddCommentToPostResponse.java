package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

@Data
public class AddCommentToPostResponse{
    private String commentId;
    private String comment;
    private String userId;
    private String postId;
    private int likes;

    public AddCommentToPostResponse(){}

    public AddCommentToPostResponse(String commentId, String comment, String userId, String postId, int likes){
        this.commentId = commentId;
        this.comment = comment;
        this.userId = userId;
        this.postId = postId;
        this.likes = likes;
    }
}
