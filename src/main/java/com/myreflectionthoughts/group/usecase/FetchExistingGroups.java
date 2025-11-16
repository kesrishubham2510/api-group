package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface FetchExistingGroups<res> {
     ResponseEntity<res> getGroups(int pageIndex, int pageSize);
}
