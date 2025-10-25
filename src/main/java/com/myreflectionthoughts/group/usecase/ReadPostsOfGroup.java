package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface ReadPostsOfGroup<res> {
    ResponseEntity<res> readPosts(String groupId, int pageIndex, int pageSize);
}
