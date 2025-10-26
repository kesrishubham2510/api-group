package com.myreflectionthoughts.group.datamodel.dto.response;

import com.myreflectionthoughts.group.datamodel.role.UserRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDetailsResponse {

    private String userId;
    private String username;
    private String joined;
    private String role;

    public UserDetailsResponse(){
        this(null, null, null, null);
    }

    public UserDetailsResponse(String userId, String username, String joined, UserRole role){
        this.userId = userId;
        this.username = username;
        this.joined = joined;
        this.role = role.name();
    }
}
