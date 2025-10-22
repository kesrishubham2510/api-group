package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface UpdatePostOfGroup <req, res>{
    ResponseEntity<res> updatePost(req request);
}
