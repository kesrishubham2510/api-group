package com.myreflectionthoughts.group.dataprovider.service;

import com.myreflectionthoughts.group.datamodel.dto.request.AddUserToGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.request.CreateDiscussionGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.response.*;
import com.myreflectionthoughts.group.datamodel.entity.DiscussionGroup;
import com.myreflectionthoughts.group.datamodel.entity.User;
import com.myreflectionthoughts.group.datamodel.entity.UserAuth;
import com.myreflectionthoughts.group.dataprovider.repository.DiscussionGroupRepository;
import com.myreflectionthoughts.group.dataprovider.repository.PostRepository;
import com.myreflectionthoughts.group.dataprovider.repository.UserRepository;
import com.myreflectionthoughts.group.exception.DiscussionGroupException;
import com.myreflectionthoughts.group.usecase.AddUserToGroup;
import com.myreflectionthoughts.group.usecase.CreateGroup;
import com.myreflectionthoughts.group.usecase.ReadGroupInformation;
import com.myreflectionthoughts.group.usecase.ReadPostsOfGroup;
import com.myreflectionthoughts.group.util.AppUtility;
import com.myreflectionthoughts.group.util.MappingUtility;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscussionGroupProvider
        implements CreateGroup<CreateDiscussionGroupRequest, DiscussionGroupMetaInfoResponse>,
        ReadGroupInformation<String, DiscussionGroupMetaInfoResponse>,
        AddUserToGroup<AddUserToGroupRequest, AddUserToGroupResponse>,
        ReadPostsOfGroup<PostsOfGroupResponse> {

    private final DiscussionGroupRepository discussionGroupRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final MappingUtility mappingUtility;

    public DiscussionGroupProvider(DiscussionGroupRepository discussionGroupRepository,
                                   UserRepository userRepository,
                                   PostRepository postRepository,
                                   MappingUtility mappingUtility){
        this.discussionGroupRepository = discussionGroupRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.mappingUtility = mappingUtility;
    }

    @Override
    public ResponseEntity<DiscussionGroupMetaInfoResponse> createDiscussionGroup(CreateDiscussionGroupRequest request) {

        DiscussionGroup discussionGroup = mappingUtility.buildDiscussionGroup(request);
        String requesterId = AppUtility.retrieveUserId();

        User user = userRepository.findById(requesterId).orElseThrow(
                ()-> new DiscussionGroupException("INVALID_CREATOR", "User:- "+requesterId+", does not exists"));

        discussionGroup.getUsers().add(user);

        discussionGroup = discussionGroupRepository.save(discussionGroup);
        HttpHeaders httpHeaders = new HttpHeaders();

        DiscussionGroupMetaInfoResponse response = mappingUtility.mapToDTO(discussionGroup);
        // the user who creates the group will be the default member of that group, when the group is created
        response.setUsers(mappingUtility.buildUserDetailsDTO(List.of(user)));
        return ResponseEntity.status(201).headers(httpHeaders).body(response);
    }

    @Override
    public ResponseEntity<DiscussionGroupMetaInfoResponse> readGroupInformation(String groupId) {
        DiscussionGroup discussionGroup =  this.discussionGroupRepository.findById(groupId)
                .orElseThrow(()-> new DiscussionGroupException("INVALID_GROUP_ID", "Discussion group with id:- "+groupId+", does not exists"));

        HttpHeaders httpHeaders = new HttpHeaders();

        DiscussionGroupMetaInfoResponse response = mappingUtility.mapToDTO(discussionGroup);
        response.setUsers(mappingUtility.buildUserDetailsResponse(discussionGroupRepository.findLatestMembersOfTheGroup(groupId)));

        return ResponseEntity.status(200).headers(httpHeaders).body(response);
    }

    @Override
    public ResponseEntity<AddUserToGroupResponse> addUserToGroup(AddUserToGroupRequest request) {

        String requesterId = AppUtility.retrieveUserId();

        DiscussionGroup discussionGroup = this.discussionGroupRepository.findById(requesterId)
                .orElseThrow(() -> new DiscussionGroupException("INVALID_GROUP_ID", "Discussion group with id:- " + request.getGroupId() + ", does not exists"));

        User user = userRepository.findById(requesterId).orElseThrow(
                () -> new DiscussionGroupException("INVALID_CREATOR", "User:- " + requesterId + ", does not exists"));

        if (!discussionGroupRepository.findMemberShip(request.getGroupId(), requesterId).isEmpty()) {
            throw new DiscussionGroupException("INVALID_REQUEST", "User:- " + requesterId + ", already a member of the group");
        }

        discussionGroup.getUsers().add(user);

        discussionGroupRepository.save(discussionGroup);

        HttpHeaders httpHeaders = new HttpHeaders();

        AddUserToGroupResponse response = new AddUserToGroupResponse();

        response.setUserId(requesterId);
        response.setDiscussionGroupId(request.getGroupId());
        response.setMemberShipId(discussionGroup.getCreatedAt());

        return ResponseEntity.status(201).headers(httpHeaders).body(response);
    }

    @Override
    public ResponseEntity<PostsOfGroupResponse> readPosts(String groupId, int pageIndex, int pageSize) {

        // TODO:- Enhance it in a way that it allows all ADMINS to execute but checks for a user's
        //  membership in the group before allowing
        // Can be implemented using the JWT

        if(pageIndex<0){
            pageIndex = 0;
        }

        if(pageSize < 3){
            pageSize = 3;
        }else if(pageSize>10){
            pageSize = 10;
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        List<PostResponse> posts = postRepository.findMyPosts(groupId, pageable).stream().collect(Collectors.toCollection(ArrayList::new));

        PostsOfGroupResponse postsOfGroupResponse = new PostsOfGroupResponse();
        postsOfGroupResponse.setGroupId(groupId);
        postsOfGroupResponse.setPosts(posts);

        HttpHeaders httpHeaders = new HttpHeaders();

        return ResponseEntity.status(201).headers(httpHeaders).body(postsOfGroupResponse);
    }
}