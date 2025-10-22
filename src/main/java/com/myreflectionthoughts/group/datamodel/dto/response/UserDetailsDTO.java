package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

@Data
public class UserDetailsDTO {

    private String userId;
    private String username;
    private String joined;
    private String role;
}
