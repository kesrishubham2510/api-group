package com.myreflectionthoughts.group.endpoint;

import com.myreflectionthoughts.group.config.RestConstant;
import com.myreflectionthoughts.group.datamodel.dto.request.*;
import com.myreflectionthoughts.group.datamodel.dto.response.AddCommentToPostResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.AddPostToGroupResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.CommentsOnPostResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.LikePostResponse;
import com.myreflectionthoughts.group.dataprovider.service.PostProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RestConstant.API_PREFIX)
public class PostController {

    private final PostProvider postProvider;

    public PostController(PostProvider postProvider){
        this.postProvider = postProvider;
    }

    @PostMapping(RestConstant.ADD_POST_IN_GROUP)
    public ResponseEntity<AddPostToGroupResponse> addPost(@PathVariable("groupId") String groupId, @RequestBody AddPostToGroupRequest addPostToGroupRequest){
        addPostToGroupRequest.setDiscussionGroupId(groupId);
        return postProvider.addPostToGroup(addPostToGroupRequest);
    }

    @PatchMapping(RestConstant.UPDATE_POST_IN_GROUP)
    public ResponseEntity<AddPostToGroupResponse> updatePost(@PathVariable("groupId") String groupId, @RequestBody UpdatePostInAGroup updatePostInAGroup){
        updatePostInAGroup.setDiscussionGroupId(groupId);
        return postProvider.updatePost(updatePostInAGroup);
    }

    @PostMapping(RestConstant.ADD_COMMENT_TO_POST)
    public ResponseEntity<AddCommentToPostResponse> addCommentToPost(@PathVariable("postId") String postId, @RequestBody AddCommentToPostRequest addCommentToPostRequest){
        addCommentToPostRequest.setPostId(postId);
        return postProvider.addComment(addCommentToPostRequest);
    }

    @PostMapping(RestConstant.LIKE_THE_POST)
    public ResponseEntity<LikePostResponse> likePost(@PathVariable("postId") String postId, @RequestBody LikePostRequest likePostRequest){
        likePostRequest.setPostId(postId);
        return postProvider.addLikeToPost(likePostRequest);
    }

    @PostMapping(RestConstant.UNLIKE_THE_POST)
    public ResponseEntity<LikePostResponse> unlikePost(@PathVariable("postId") String postId, @RequestBody UnLikePostRequest unLikePostRequest){
        unLikePostRequest.setPostId(postId);
        return postProvider.removeLikeFromPost(unLikePostRequest);
    }

    @PostMapping(RestConstant.LIKE_COMMENT_ON_THE_POST)
    public ResponseEntity<LikePostResponse> likeCommentOnPost(@PathVariable("postId") String postId, @PathVariable("commentId") String commentId, @RequestBody LikeCommentOnPostRequest likeCommentOnPostRequest){
        likeCommentOnPostRequest.setPostId(postId);
        likeCommentOnPostRequest.setCommentId(commentId);
        return postProvider.addLikeToComment(likeCommentOnPostRequest);
    }

    @PostMapping(RestConstant.UNLIKE_COMMENT_ON_THE_POST)
    public ResponseEntity<LikePostResponse> unlikeCommentOnPost(@PathVariable("postId") String postId, @PathVariable("commentId") String commentId, @RequestBody UnlikeCommentOnPostRequest unlikeCommentOnPostRequest){
        unlikeCommentOnPostRequest.setPostId(postId);
        unlikeCommentOnPostRequest.setCommentId(commentId);
        return postProvider.removeLikeFromComment(unlikeCommentOnPostRequest);
    }

    @GetMapping(RestConstant.READ_COMMENT_OF_A_POST)
    public ResponseEntity<CommentsOnPostResponse> unlikeCommentOnPost(@PathVariable("postId") String postId, @PathVariable("groupId") String groupId,
                                                                      @RequestParam(value = "pageIndex", defaultValue = "0", required = false) int pageIndex,
                                                                      @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize){
        return postProvider.readCommentsOnAPost(groupId, postId, pageSize, pageIndex);
    }



}
