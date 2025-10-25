package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface RemoveLikeFromPost<req, res> {
    ResponseEntity<res> removeLikeFromPost(req request);
}
