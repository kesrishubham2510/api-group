package com.myreflectionthoughts.group.datamodel.dto.request;

import lombok.Data;


@Data
public class CreateDiscussionGroupRequest {
    private String groupName;
    private String description;
}
