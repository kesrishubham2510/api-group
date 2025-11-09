package com.myreflectionthoughts.group.datamodel.dto.response;

import lombok.Data;

@Data
public class LikeDetailsDTO {
    private String userId;
    private String likeId;

    public LikeDetailsDTO(String userId, String likeId){
        this.userId = userId;
        this.likeId = likeId;
    }
}