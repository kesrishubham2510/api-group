package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface UserCanRaiseRequestToJoinGroup<req, res> {
    ResponseEntity<res> raiseRequest(req request);
}
