package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class DiscussionGroupMetaInfoDTO {
    private String groupId;
    private String groupName;
    private List<UserDetailsDTO> users;
    private String createdAt;
    private String description;
}
