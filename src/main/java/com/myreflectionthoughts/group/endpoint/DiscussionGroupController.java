package com.myreflectionthoughts.group.endpoint;

import com.myreflectionthoughts.group.config.RestConstant;
import com.myreflectionthoughts.group.datamodel.dto.request.AddUserToGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.request.CreateDiscussionGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.response.AddUserToGroupResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.DiscussionGroupMetaInfoResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.PostsOfGroupResponse;
import com.myreflectionthoughts.group.dataprovider.service.DiscussionGroupProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DiscussionGroupMetaInfoResponse> createDiscussionGroup(@RequestBody CreateDiscussionGroupRequest createDiscussionGroupRequest){
        return discussionGroupProvider.createDiscussionGroup(createDiscussionGroupRequest);
    }


    // This endpoint will be accessible to all roles
    @GetMapping(RestConstant.READ_GROUP)
    @PreAuthorize("hasAuthority('ADMIN', 'USER')")
    public ResponseEntity<DiscussionGroupMetaInfoResponse> createDiscussionGroup(@PathVariable("groupId") String groupId){
        return discussionGroupProvider.readGroupInformation(groupId);
    }

    @PostMapping(RestConstant.ADD_USER_TO_GROUP)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AddUserToGroupResponse> addUserToGroup(@RequestBody AddUserToGroupRequest addUserToGroupRequest){
        return discussionGroupProvider.addUserToGroup(addUserToGroupRequest);
    }

    // will be accessible to all ADMIN user, and user who is part of the group
    @GetMapping(RestConstant.READ_POSTS_OF_A_GROUP)
    @PreAuthorize("hasAuthority('ADMIN', 'USER')")
    public ResponseEntity<PostsOfGroupResponse> getPostsOfGroup(
            @PathVariable("groupId") String groupId,
            @RequestParam(value = "pageIndex", defaultValue = "0", required = false) int pageInd,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize){

        return discussionGroupProvider.readPosts(groupId, pageInd, pageSize);
    }

}
