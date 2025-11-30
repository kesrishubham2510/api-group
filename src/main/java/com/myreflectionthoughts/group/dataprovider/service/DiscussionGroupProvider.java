package com.myreflectionthoughts.group.dataprovider.service;

import com.myreflectionthoughts.group.datamodel.dto.request.AddUserToGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.request.CreateDiscussionGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.request.RaiseGroupJoinRequest;
import com.myreflectionthoughts.group.datamodel.dto.response.*;
import com.myreflectionthoughts.group.datamodel.entity.AdmissionRequest;
import com.myreflectionthoughts.group.datamodel.entity.DiscussionGroup;
import com.myreflectionthoughts.group.datamodel.entity.User;
import com.myreflectionthoughts.group.datamodel.entity.UserAuth;
import com.myreflectionthoughts.group.datamodel.role.UserRole;
import com.myreflectionthoughts.group.dataprovider.repository.DiscussionGroupRepository;
import com.myreflectionthoughts.group.dataprovider.repository.AdmissionRequestRepository;
import com.myreflectionthoughts.group.dataprovider.repository.PostLikeRepository;
import com.myreflectionthoughts.group.dataprovider.repository.PostRepository;
import com.myreflectionthoughts.group.dataprovider.repository.UserRepository;
import com.myreflectionthoughts.group.exception.DiscussionGroupException;
import com.myreflectionthoughts.group.usecase.*;
import com.myreflectionthoughts.group.util.AppUtility;
import com.myreflectionthoughts.group.util.MappingUtility;
import org.springframework.data.domain.Page;
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
        ReadPostsOfGroup<PostsOfGroupResponse>,
        UserCanRaiseRequestToJoinGroup<RaiseGroupJoinRequest, GroupJoinResponse>,
        FetchExistingGroups<List<DiscussionGroupMetaInfoResponse>> {

    private final DiscussionGroupRepository discussionGroupRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final MappingUtility mappingUtility;
    private final AdmissionRequestRepository admissionRequestRepository;
    private final PostLikeRepository postLikeRepository;

    public DiscussionGroupProvider(DiscussionGroupRepository discussionGroupRepository,
                                   UserRepository userRepository,
                                   PostRepository postRepository,
                                   AdmissionRequestRepository admissionRequestRepository,
                                   PostLikeRepository postLikeRepository,
                                   MappingUtility mappingUtility){
        this.discussionGroupRepository = discussionGroupRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.admissionRequestRepository = admissionRequestRepository;
        this.postLikeRepository = postLikeRepository;
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
        response.setUsers(new ArrayList<>());
        response.setGroupAdmin(mappingUtility.buildUserDetailsDTO(List.of(user)).get(0));
        return ResponseEntity.status(201).headers(httpHeaders).body(response);
    }

    @Override
    public ResponseEntity<DiscussionGroupMetaInfoResponse> readGroupInformation(String groupId) {
        DiscussionGroup discussionGroup =  this.discussionGroupRepository.findById(groupId)
                .orElseThrow(()-> new DiscussionGroupException("INVALID_GROUP_ID", "Discussion group with id:- "+groupId+", does not exists"));

        HttpHeaders httpHeaders = new HttpHeaders();

        DiscussionGroupMetaInfoResponse response = mappingUtility.mapToDTO(discussionGroup);

        List<Object[]> groupAdmin = discussionGroupRepository.findTheAdmin(groupId);
        response.setGroupAdmin(mappingUtility.buildUserDetailsResponse(groupAdmin).get(0));
        response.setUsers(mappingUtility.buildUserDetailsResponse(discussionGroupRepository.findLatestMembersOfTheGroup(groupId)));

        return ResponseEntity.status(200).headers(httpHeaders).body(response);
    }

    @Override
    public ResponseEntity<AddUserToGroupResponse> addUserToGroup(AddUserToGroupRequest request) {

        String requesterId = AppUtility.retrieveUserId();

        DiscussionGroup discussionGroup = this.discussionGroupRepository.findById(request.getGroupId())
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

        String requesterId = AppUtility.retrieveUserId();

        if(((UserAuth)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getRole().compareTo(UserRole.USER)==0) {

             if (discussionGroupRepository.findMemberShip(groupId, requesterId).isEmpty()) {
                throw new DiscussionGroupException("BAD_REQUEST", "User:- " + requesterId + ", not a member of the group");
            }
        }

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
        populateLikeDetails(groupId, posts, requesterId);
        PostsOfGroupResponse postsOfGroupResponse = new PostsOfGroupResponse();
        postsOfGroupResponse.setGroupId(groupId);
        postsOfGroupResponse.setPosts(posts);

        HttpHeaders httpHeaders = new HttpHeaders();

        return ResponseEntity.status(201).headers(httpHeaders).body(postsOfGroupResponse);
    }

    @Override
    public ResponseEntity<GroupJoinResponse> raiseRequest(RaiseGroupJoinRequest request) {

        String userId = AppUtility.retrieveUserId();
        GroupJoinResponse groupJoinResponse = new GroupJoinResponse();
        groupJoinResponse.setMessage("You can't join a group you are already part of.");

        groupJoinResponse.setGroupId(request.getGroupId());

        this.discussionGroupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new DiscussionGroupException("INVALID_GROUP_ID", "Discussion group with id:- " + request.getGroupId() + ", does not exists"));

        try{
            checkMemberShip(request.getGroupId(), userId);
        }catch (DiscussionGroupException ex){



            AdmissionRequest admissionRequest = new AdmissionRequest();
            admissionRequest.setGroupId(request.getGroupId());
            admissionRequest.setUserId(userId);

            List<AdmissionRequest> admissionRequests = admissionRequestRepository.findByUserIdAndGroupId(userId, admissionRequest.getGroupId());

            if(admissionRequests.isEmpty()) {
                // save the request
                admissionRequest = admissionRequestRepository.save(admissionRequest);
            }else{
                admissionRequest = admissionRequests.get(0);
            }

            groupJoinResponse.setRequestId(admissionRequest.getRequestId());
            groupJoinResponse.setMessage("Your request has been entertained");
        }

        HttpHeaders httpHeaders = new HttpHeaders();

        return ResponseEntity.status(201).headers(httpHeaders).body(groupJoinResponse);
    }

    @Override
    public ResponseEntity<List<DiscussionGroupMetaInfoResponse>> getGroups(int pageIndex, int pageSize) {

        if(pageIndex < 0){
            pageIndex = 1;
        }

        if(pageSize < 3){
            pageSize = 3;
        }

        if(pageSize > 10){
            pageSize = 10;
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<DiscussionGroup> groupPage = discussionGroupRepository.findAll(pageable);

        List<DiscussionGroupMetaInfoResponse> response = groupPage.stream().map(discussionGroup -> {
            DiscussionGroupMetaInfoResponse discussionGroupResponse = new DiscussionGroupMetaInfoResponse();

            discussionGroupResponse.setGroupId(discussionGroup.getGroupId());
            discussionGroupResponse.setGroupName(discussionGroup.getGroupName());
            discussionGroupResponse.setUsers(new ArrayList<>());
            discussionGroupResponse.setDescription(discussionGroup.getDescription());
            discussionGroupResponse.setCreatedAt(discussionGroup.getCreatedAt());

            UserDetailsResponse groupAdmin = mappingUtility.buildUserDetailsResponse(discussionGroupRepository.findTheAdmin(discussionGroup.getGroupId())).get(0);

            try{
                checkMemberShip(discussionGroupResponse.getGroupId(), AppUtility.retrieveUserId());
                discussionGroupResponse.setAmIAMember(true);
            }catch(DiscussionGroupException exception){
                discussionGroupResponse.setAmIAMember(false);
            }

            discussionGroupResponse.setGroupAdmin(groupAdmin);

            return discussionGroupResponse;
        }).collect(Collectors.toCollection(ArrayList::new));


        HttpHeaders httpHeaders = new HttpHeaders();

        return ResponseEntity.status(200).headers(httpHeaders).body(response);
    }

    private void populateLikeDetails(String groupId, List<PostResponse> responses, String requesterId){
        responses.forEach(response-> response.setHaveILiked(!postLikeRepository.findLikeDetailsForThePost(response.getPostId(), groupId, requesterId).isEmpty()));
    }

    private void checkMemberShip(String discussionGroupId, String userId){
        if(discussionGroupRepository.findMemberShip(discussionGroupId, userId).isEmpty()){
            throw new DiscussionGroupException("INVALID_REQUEST", "User:- "+userId+", is not a member of the group");
        }

    }
}