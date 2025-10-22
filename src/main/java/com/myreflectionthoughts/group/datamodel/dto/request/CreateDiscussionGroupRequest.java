package com.myreflectionthoughts.group.datamodel.dto.request;

import lombok.Data;


@Data
public class CreateDiscussionGroupRequest {
    private String creatorId;
    private String groupName;
    private String description;
}
