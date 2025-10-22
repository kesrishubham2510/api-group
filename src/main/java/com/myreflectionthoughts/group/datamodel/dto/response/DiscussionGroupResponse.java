package com.myreflectionthoughts.group.datamodel.dto.response;

import com.myreflectionthoughts.group.datamodel.entity.Post;
import com.myreflectionthoughts.group.datamodel.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class DiscussionGroupResponse {
    private String groupId;
    private String groupName;
    private List<User> users;
    private List<Post> posts;
    private String createdAt;
    private String description;
}
