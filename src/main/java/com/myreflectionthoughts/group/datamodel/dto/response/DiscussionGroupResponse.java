package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class DiscussionGroupResponse {
    private String groupId;
    private String groupName;
    private List<UserDetailsResponse> users;
    private List<PostResponse> posts;
    private String createdAt;
    private String description;
}
