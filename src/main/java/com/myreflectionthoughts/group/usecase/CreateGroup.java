package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface CreateGroup<req, res> {

    ResponseEntity<res> createDiscussionGroup(req request);
}
