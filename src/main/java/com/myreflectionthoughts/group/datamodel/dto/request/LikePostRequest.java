package com.myreflectionthoughts.group.datamodel.dto.request;

import lombok.Data;

@Data
public class LikePostRequest {

    private String userId;
    private String postId;
    private String groupId;
}
