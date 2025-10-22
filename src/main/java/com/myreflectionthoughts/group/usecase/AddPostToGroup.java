package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface AddPostToGroup <req, res>{
    ResponseEntity<res> addPostToGroup(req request);
}
