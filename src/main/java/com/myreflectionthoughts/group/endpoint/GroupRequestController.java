package com.myreflectionthoughts.group.endpoint;

import com.myreflectionthoughts.group.config.RestConstant;
import com.myreflectionthoughts.group.datamodel.dto.request.RaiseGroupJoinRequest;
import com.myreflectionthoughts.group.datamodel.dto.response.GroupJoinResponse;
import com.myreflectionthoughts.group.dataprovider.service.DiscussionGroupProvider;
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

    @PostMapping(RestConstant.RAISE_REQUEST_TO_JOIN_GROUP)
    public ResponseEntity<GroupJoinResponse> raiseGroupAdmissionRequest(@RequestBody RaiseGroupJoinRequest request){
        return discussionGroupProvider.raiseRequest(request);
    }

}
