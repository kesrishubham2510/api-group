package com.myreflectionthoughts.group.endpoint;

import com.myreflectionthoughts.group.config.RestConstant;
import com.myreflectionthoughts.group.datamodel.dto.request.RaiseGroupJoinRequest;
import com.myreflectionthoughts.group.datamodel.dto.response.DiscussionGroupMetaInfoResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.GroupJoinResponse;
import com.myreflectionthoughts.group.dataprovider.service.DiscussionGroupProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestConstant.API_PREFIX)
public class GroupRequestController {

    private final DiscussionGroupProvider discussionGroupProvider;

    public GroupRequestController(DiscussionGroupProvider discussionGroupProvider){
        this.discussionGroupProvider = discussionGroupProvider;
    }

    @Operation(
            summary = "Create a new discussion group join request",
            description = "Creates a new discussion group join request.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created the discussion group join request",
                            content = @Content(schema = @Schema(implementation = GroupJoinResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized — JWT missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden — user does not authority to perform the action"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping(RestConstant.RAISE_REQUEST_TO_JOIN_GROUP)
    public ResponseEntity<GroupJoinResponse> raiseGroupAdmissionRequest(@RequestBody RaiseGroupJoinRequest request){
        return discussionGroupProvider.raiseRequest(request);
    }

}
