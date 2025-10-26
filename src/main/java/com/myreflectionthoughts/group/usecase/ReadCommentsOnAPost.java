package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface ReadCommentsOnAPost<res> {
    ResponseEntity<res> readCommentsOnAPost(String groupId, String postId, String userId);
}
