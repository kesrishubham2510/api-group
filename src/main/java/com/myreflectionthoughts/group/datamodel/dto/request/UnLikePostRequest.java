package com.myreflectionthoughts.group.datamodel.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UnLikePostRequest extends LikePostRequest{
    private String LikeId;
}
