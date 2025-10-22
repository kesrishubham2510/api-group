package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface AddLikeToPost <req, res>{
    ResponseEntity<res> addLike(req request);
}
