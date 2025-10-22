package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface ReadPostOfGroup<res> {
     ResponseEntity<res> readPost(String userId, String postId);
}
