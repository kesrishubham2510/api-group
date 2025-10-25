package com.myreflectionthoughts.group.datamodel.dto.response;

import com.myreflectionthoughts.group.datamodel.role.UserRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDetailsDTO {

    private String userId;
    private String username;
    private String joined;
    private String role;

    public UserDetailsDTO(){
        this(null, null, null, null);
    }

    public UserDetailsDTO(String userId, String username, String joined, UserRole role){
        this.userId = userId;
        this.username = username;
        this.joined = joined;
        this.role = role.name();
    }
}
