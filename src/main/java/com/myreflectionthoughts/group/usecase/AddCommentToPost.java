package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface AddCommentToPost<req, res> {

    ResponseEntity<res> addComment(req request);
}
