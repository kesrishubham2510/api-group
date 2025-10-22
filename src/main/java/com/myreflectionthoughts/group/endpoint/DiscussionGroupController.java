package com.myreflectionthoughts.group.endpoint;

import com.myreflectionthoughts.group.config.RestConstant;
import com.myreflectionthoughts.group.datamodel.dto.request.AddPostToGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.request.AddUserToGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.request.CreateDiscussionGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.response.AddPostToGroupResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.AddUserToGroupDTO;
import com.myreflectionthoughts.group.datamodel.dto.response.DiscussionGroupMetaInfoDTO;
import com.myreflectionthoughts.group.dataprovider.service.DiscussionGroupProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// All end points will be accessible to ADMIN user only
@RestController
@RequestMapping(RestConstant.API_PREFIX)
public class DiscussionGroupController {

    private final DiscussionGroupProvider discussionGroupProvider;

    public DiscussionGroupController(DiscussionGroupProvider discussionGroupProvider){
        this.discussionGroupProvider = discussionGroupProvider;
    }

    @PostMapping(RestConstant.CREATE_GROUP)
    public ResponseEntity<DiscussionGroupMetaInfoDTO> createDiscussionGroup(@RequestBody CreateDiscussionGroupRequest createDiscussionGroupRequest){
        return discussionGroupProvider.createDiscussionGroup(createDiscussionGroupRequest);
    }


    // This endpoint will be accessible to all roles
    @GetMapping(RestConstant.READ_GROUP)
    public ResponseEntity<DiscussionGroupMetaInfoDTO> createDiscussionGroup(@PathVariable("groupId") String groupId){
        return discussionGroupProvider.readGroupInformation(groupId);
    }

    @PostMapping(RestConstant.ADD_USER_TO_GROUP)
    public ResponseEntity<AddUserToGroupDTO> addUserToGroup(@RequestBody AddUserToGroupRequest addUserToGroupRequest){
        return discussionGroupProvider.addUserToGroup(addUserToGroupRequest);
    }

    //  // This endpoint will be accessible to all roles, the user should be part of the group
    @PostMapping(RestConstant.ADD_POST_IN_GROUP)
    public ResponseEntity<AddPostToGroupResponse> addPost(@RequestBody AddPostToGroupRequest addPostToGroupRequest){
        return discussionGroupProvider.addPostToGroup(addPostToGroupRequest);
    }

}
