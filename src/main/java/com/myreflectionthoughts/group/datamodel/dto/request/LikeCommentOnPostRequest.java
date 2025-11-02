package com.myreflectionthoughts.group.datamodel.dto.request;

import lombok.Data;

@Data
public class LikeCommentOnPostRequest {
    private String groupId;
    private String commentId;
    private String postId;
}
