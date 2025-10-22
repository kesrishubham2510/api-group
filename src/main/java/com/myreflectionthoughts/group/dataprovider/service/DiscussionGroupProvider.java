package com.myreflectionthoughts.group.dataprovider.service;

import com.myreflectionthoughts.group.datamodel.dto.request.CreateDiscussionGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.response.DiscussionGroupResponse;
import com.myreflectionthoughts.group.datamodel.entity.DiscussionGroup;
import com.myreflectionthoughts.group.dataprovider.repository.DiscussionGroupRepository;
import com.myreflectionthoughts.group.exception.DiscussionGroupException;
import com.myreflectionthoughts.group.usecase.CreateGroup;
import com.myreflectionthoughts.group.usecase.ReadGroupInformation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

@Service
public class DiscussionGroupProvider
        implements CreateGroup<CreateDiscussionGroupRequest, DiscussionGroupResponse>,
        ReadGroupInformation<String, DiscussionGroupResponse>
{

    private final DiscussionGroupRepository discussionGroupRepository;

    public DiscussionGroupProvider(DiscussionGroupRepository discussionGroupRepository){
        this.discussionGroupRepository = discussionGroupRepository;
    }

    @Override
    public ResponseEntity<DiscussionGroupResponse> createDiscussionGroup(CreateDiscussionGroupRequest request) {

        DiscussionGroup discussionGroup = buildDiscussionGroup(request);
        discussionGroup = discussionGroupRepository.save(discussionGroup);
        HttpHeaders httpHeaders = new HttpHeaders();
        return ResponseEntity.status(201).headers(httpHeaders).body(mapToDTO(discussionGroup));
    }

    private DiscussionGroup buildDiscussionGroup(CreateDiscussionGroupRequest createDiscussionGroupRequest){
        DiscussionGroup discussionGroup = new DiscussionGroup();

        discussionGroup.setCreatedAt(String.valueOf(Instant.now().getEpochSecond()));
        discussionGroup.setDescription(createDiscussionGroupRequest.getDescription());
        discussionGroup.setGroupName(createDiscussionGroupRequest.getGroupName());
        discussionGroup.setUsers(new ArrayList<>());
        discussionGroup.setPosts(new ArrayList<>());

        return discussionGroup;
    }

    private DiscussionGroupResponse mapToDTO(DiscussionGroup discussionGroup){

        DiscussionGroupResponse response = new DiscussionGroupResponse();

        response.setCreatedAt(discussionGroup.getCreatedAt());
        response.setDescription(discussionGroup.getDescription());
        response.setGroupName(discussionGroup.getGroupName());
        response.setUsers(discussionGroup.getUsers());
        response.setPosts(discussionGroup.getPosts());
        response.setGroupId(discussionGroup.getGroupId());

        return response;
    }

    @Override
    public ResponseEntity<DiscussionGroupResponse> readGroupInformation(String groupId) {
        DiscussionGroup discussionGroup =  this.discussionGroupRepository.findById(groupId)
                .orElseThrow(()-> new DiscussionGroupException("INVALID_GROUP_ID", "Discussion group with id:- "+groupId+", does not exists"));
        HttpHeaders httpHeaders = new HttpHeaders();
        return ResponseEntity.status(201).headers(httpHeaders).body(mapToDTO(discussionGroup));
    }
}