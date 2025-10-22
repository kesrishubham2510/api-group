package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

@Data
public class AddUserToGroupDTO {
    private String userId;
    private String discussionGroupId;
    private String memberShipId;
}