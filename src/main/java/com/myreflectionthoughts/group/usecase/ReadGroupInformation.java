package com.myreflectionthoughts.group.usecase;

import org.springframework.http.ResponseEntity;

public interface ReadGroupInformation<reqParam, res> {
    ResponseEntity<res> readGroupInformation(reqParam reqParam);
}
