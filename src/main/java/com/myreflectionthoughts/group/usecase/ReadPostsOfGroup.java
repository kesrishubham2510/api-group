package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface ReadPostsOfGroup<req, res> {
    ResponseEntity<res> readPosts(req request);
}
