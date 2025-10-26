package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PostsOfGroupResponse {
    private String groupId;
    private List<PostResponse> posts;
}
