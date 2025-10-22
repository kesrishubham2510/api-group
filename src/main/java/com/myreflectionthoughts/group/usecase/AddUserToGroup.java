package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface AddUserToGroup<req, res>{

    ResponseEntity<res> addUserToGroup(req request);
}
