package com.myreflectionthoughts.group.datamodel.dto.response;

import com.myreflectionthoughts.group.datamodel.dto.request.RaiseGroupJoinRequest;
import lombok.Data;

@Data
public class GroupJoinResponse extends RaiseGroupJoinRequest {
    private String requestId;
    private String message;
}