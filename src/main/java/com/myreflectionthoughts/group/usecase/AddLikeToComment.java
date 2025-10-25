package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface AddLikeToComment<req, res> {
    ResponseEntity<res> addLikeToComment(req req);
}
