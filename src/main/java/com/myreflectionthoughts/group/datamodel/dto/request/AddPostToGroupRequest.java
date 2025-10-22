package com.myreflectionthoughts.group.datamodel.dto.request;

import lombok.Data;

@Data
public class AddPostToGroupRequest {
    private String content;
    private String discussionGroupId;
    private String authorId;
}