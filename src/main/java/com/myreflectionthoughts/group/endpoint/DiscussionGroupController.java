package com.myreflectionthoughts.group.endpoint;

import com.myreflectionthoughts.group.config.RestConstant;
import com.myreflectionthoughts.group.datamodel.dto.request.CreateDiscussionGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.response.DiscussionGroupResponse;
import com.myreflectionthoughts.group.dataprovider.service.DiscussionGroupProvider;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RestConstant.API_PREFIX)
public class DiscussionGroupController {

    private final DiscussionGroupProvider discussionGroupProvider;

    public DiscussionGroupController(DiscussionGroupProvider discussionGroupProvider){
        this.discussionGroupProvider = discussionGroupProvider;
    }

    @PostMapping(RestConstant.CREATE_GROUP)
    public ResponseEntity<DiscussionGroupResponse> createDiscussionGroup(@RequestBody CreateDiscussionGroupRequest createDiscussionGroupRequest){
        return discussionGroupProvider.createDiscussionGroup(createDiscussionGroupRequest);
    }

    @GetMapping(RestConstant.READ_GROUP)
    public ResponseEntity<DiscussionGroupResponse> createDiscussionGroup(@PathVariable("groupId") String groupId){
        return discussionGroupProvider.readGroupInformation(groupId);
    }

}
