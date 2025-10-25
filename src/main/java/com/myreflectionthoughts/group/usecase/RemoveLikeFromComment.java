package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface RemoveLikeFromComment<req, res>{
    ResponseEntity<res> removeLikeFromComment(req req);
}
