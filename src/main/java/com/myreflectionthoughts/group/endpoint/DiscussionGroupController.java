package com.myreflectionthoughts.group.endpoint;

import com.myreflectionthoughts.group.config.RestConstant;
import com.myreflectionthoughts.group.datamodel.dto.request.AddUserToGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.request.CreateDiscussionGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.response.AddUserToGroupResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.DiscussionGroupMetaInfoResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.DiscussionGroupResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.PostsOfGroupResponse;
import com.myreflectionthoughts.group.dataprovider.service.DiscussionGroupProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.websocket.server.PathParam;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(RestConstant.API_PREFIX)
public class DiscussionGroupController {

    private final DiscussionGroupProvider discussionGroupProvider;

    public DiscussionGroupController(DiscussionGroupProvider discussionGroupProvider){
        this.discussionGroupProvider = discussionGroupProvider;
    }

    @Operation(
            summary = "Create a new discussion group",
            description = "Creates a new discussion group. Only users with ADMIN authority can access this endpoint.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created the discussion group",
                            content = @Content(schema = @Schema(implementation = DiscussionGroupMetaInfoResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized — JWT missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden — user does not have ADMIN authority"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping(RestConstant.CREATE_GROUP)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DiscussionGroupMetaInfoResponse> createDiscussionGroup(@RequestBody CreateDiscussionGroupRequest createDiscussionGroupRequest){
        return discussionGroupProvider.createDiscussionGroup(createDiscussionGroupRequest);
    }


    // This endpoint will be accessible to all roles
    @GetMapping(RestConstant.READ_GROUP)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<PostsOfGroupResponse> getPostsOfGroup(
            @PathVariable("groupId") String groupId,
            @RequestParam(value = "pageIndex", defaultValue = "0", required = false) int pageInd,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize){

        return discussionGroupProvider.readPosts(groupId, pageInd, pageSize);
    }

    @GetMapping(RestConstant.READ_GROUPS)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<DiscussionGroupMetaInfoResponse>> getGroups(
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") int pageSize){
        return discussionGroupProvider.getGroups(pageIndex, pageSize);
    }
}
