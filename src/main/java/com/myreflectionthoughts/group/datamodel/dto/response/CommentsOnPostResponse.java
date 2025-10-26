package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CommentsOnPostResponse{
    private String postId;
    private List<AddCommentToPostResponse> comments;
}
