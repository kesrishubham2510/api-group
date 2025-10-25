package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PostsOfGroupDTO {
    private String groupId;
    private List<PostDTO> posts;
}
