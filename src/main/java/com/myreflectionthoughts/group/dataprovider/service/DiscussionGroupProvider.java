package com.myreflectionthoughts.group.dataprovider.service;

import com.myreflectionthoughts.group.datamodel.dto.request.AddPostToGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.request.AddUserToGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.request.CreateDiscussionGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.response.*;
import com.myreflectionthoughts.group.datamodel.entity.DiscussionGroup;
import com.myreflectionthoughts.group.datamodel.entity.Post;
import com.myreflectionthoughts.group.datamodel.entity.User;
import com.myreflectionthoughts.group.dataprovider.repository.DiscussionGroupRepository;
import com.myreflectionthoughts.group.dataprovider.repository.PostRepository;
import com.myreflectionthoughts.group.dataprovider.repository.UserRepository;
import com.myreflectionthoughts.group.exception.DiscussionGroupException;
import com.myreflectionthoughts.group.usecase.AddPostToGroup;
import com.myreflectionthoughts.group.usecase.AddUserToGroup;
import com.myreflectionthoughts.group.usecase.CreateGroup;
import com.myreflectionthoughts.group.usecase.ReadGroupInformation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscussionGroupProvider
        implements CreateGroup<CreateDiscussionGroupRequest, DiscussionGroupMetaInfoDTO>,
        ReadGroupInformation<String, DiscussionGroupMetaInfoDTO>,
        AddUserToGroup<AddUserToGroupRequest, AddUserToGroupDTO>,
        AddPostToGroup<AddPostToGroupRequest, AddPostToGroupResponse>
{

    private final DiscussionGroupRepository discussionGroupRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public DiscussionGroupProvider(DiscussionGroupRepository discussionGroupRepository,
                                   UserRepository userRepository,
                                   PostRepository postRepository){
        this.discussionGroupRepository = discussionGroupRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public ResponseEntity<DiscussionGroupMetaInfoDTO> createDiscussionGroup(CreateDiscussionGroupRequest request) {

        DiscussionGroup discussionGroup = buildDiscussionGroup(request);

        User user = userRepository.findById(request.getCreatorId()).orElseThrow(
                ()-> new DiscussionGroupException("INVALID_CREATOR", "User:- "+request.getCreatorId()+", does not exists"));

        discussionGroup.getUsers().add(user);

        discussionGroup = discussionGroupRepository.save(discussionGroup);
        HttpHeaders httpHeaders = new HttpHeaders();
        return ResponseEntity.status(201).headers(httpHeaders).body(mapToDTO(discussionGroup));
    }

    @Override
    public ResponseEntity<DiscussionGroupMetaInfoDTO> readGroupInformation(String groupId) {
        DiscussionGroup discussionGroup =  this.discussionGroupRepository.findById(groupId)
                .orElseThrow(()-> new DiscussionGroupException("INVALID_GROUP_ID", "Discussion group with id:- "+groupId+", does not exists"));
        HttpHeaders httpHeaders = new HttpHeaders();
        return ResponseEntity.status(201).headers(httpHeaders).body(mapToDTO(discussionGroup));
    }

    @Override
    public ResponseEntity<AddUserToGroupDTO> addUserToGroup(AddUserToGroupRequest request) {

        DiscussionGroup discussionGroup = this.discussionGroupRepository.findById(request.getGroupId())
                .orElseThrow(()-> new DiscussionGroupException("INVALID_GROUP_ID", "Discussion group with id:- "+request.getGroupId()+", does not exists"));

        User user = userRepository.findById(request.getUserId()).orElseThrow(
                ()-> new DiscussionGroupException("INVALID_CREATOR", "User:- "+request.getUserId()+", does not exists"));

        if(!discussionGroupRepository.findMemberShip(request.getGroupId(), request.getUserId()).isEmpty()){
            throw new DiscussionGroupException("INVALID_REQUEST", "User:- "+request.getUserId()+", already a member of the group");
        }

        discussionGroup.getUsers().add(user);

        discussionGroupRepository.save(discussionGroup);

        HttpHeaders httpHeaders = new HttpHeaders();

        AddUserToGroupDTO response = new AddUserToGroupDTO();

        response.setUserId(request.getUserId());
        response.setDiscussionGroupId(request.getGroupId());
        response.setMemberShipId(discussionGroup.getCreatedAt());

        return ResponseEntity.status(201).headers(httpHeaders).body(response);
    }

    @Override
    public ResponseEntity<AddPostToGroupResponse> addPostToGroup(AddPostToGroupRequest request) {

        // Add a custom query to check if the user is a part of the group
        /*
           ---> Check the membership table has a combination for groupId and memberId

        */

        if(discussionGroupRepository.findMemberShip(request.getDiscussionGroupId(), request.getAuthorId()).isEmpty()){
            throw new DiscussionGroupException("INVALID_REQUEST", "User:- "+request.getAuthorId()+", is not a member of the group");
        }

        DiscussionGroup discussionGroup = this.discussionGroupRepository.findById(request.getDiscussionGroupId())
                .orElseThrow(()-> new DiscussionGroupException("INVALID_GROUP_ID", "Discussion group with id:- "+request.getDiscussionGroupId()+", does not exists"));

        User user = userRepository.findById(request.getAuthorId()).orElseThrow(
                ()-> new DiscussionGroupException("INVALID_CREATOR", "User:- "+request.getAuthorId()+", does not exists"));

        Post post = buildPost(request.getContent());
        post.setUser(user);
        post.setDiscussionGroup(discussionGroup);

        post = postRepository.save(post);

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.status(201).headers(headers).body(buildPostDTO(post));
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

    private List<UserDetailsDTO> buildUserDetailsDTO(List<User> userList){

        return userList.stream().map(user -> {
             UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
             userDetailsDTO.setUserId(user.getUserId());
             userDetailsDTO.setUsername(user.getUsername());
             userDetailsDTO.setRole(user.getRole().name());
             userDetailsDTO.setJoined(user.getJoined());

             return userDetailsDTO;
        }).collect(Collectors.toList());
    }

    private List<PostDTO> buildPostDTO(List<Post> postList){

        return postList.stream().map(user -> {
            PostDTO postDTO = new PostDTO();
            postDTO.setPostId(postDTO.getPostId());
            postDTO.setContent(postDTO.getContent());
            postDTO.setAuthor(new UserDetailsDTO());
            postDTO.setLikes(0);

            return postDTO;
        }).collect(Collectors.toList());
    }


    private DiscussionGroupMetaInfoDTO mapToDTO(DiscussionGroup discussionGroup){

        DiscussionGroupMetaInfoDTO response = new DiscussionGroupMetaInfoDTO();

        response.setCreatedAt(discussionGroup.getCreatedAt());
        response.setDescription(discussionGroup.getDescription());
        response.setGroupName(discussionGroup.getGroupName());
        response.setUsers(buildUserDetailsDTO(discussionGroup.getUsers()));
        response.setGroupId(discussionGroup.getGroupId());

        return response;
    }

    private Post buildPost(String content){
        Post post = new Post();
        post.setPostedAt(String.valueOf(Instant.now()));
        post.setContent(content);
        post.setComments(new ArrayList<>());
        post.setLikes(new ArrayList<>());
        return post;
    }

    private AddPostToGroupResponse buildPostDTO(Post post){
        AddPostToGroupResponse postDTO = new AddPostToGroupResponse();
        postDTO.setPostId(post.getPostId());
        postDTO.setPostedAt(post.getPostedAt());
        postDTO.setContent(post.getContent());
        postDTO.setLikes(0);
        postDTO.setAuthor(buildUserDetailsDTO(List.of(post.getUser())).get(0));
        return postDTO;
    }
}