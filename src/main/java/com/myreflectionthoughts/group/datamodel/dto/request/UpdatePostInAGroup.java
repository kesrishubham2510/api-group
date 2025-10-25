package com.myreflectionthoughts.group.datamodel.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdatePostInAGroup extends AddPostToGroupRequest{
    private String postId;
}
