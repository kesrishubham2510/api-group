package com.myreflectionthoughts.group.datamodel.dto.request;

import lombok.Data;

@Data
public class AddUserToGroupRequest {
    private String userId;
    private String groupId;
}