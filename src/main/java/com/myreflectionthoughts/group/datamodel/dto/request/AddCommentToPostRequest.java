package com.myreflectionthoughts.group.datamodel.dto.request;

import lombok.Data;

@Data
public class AddCommentToPostRequest {
    private String comment;
    private String postId;
    private String groupId;
}
