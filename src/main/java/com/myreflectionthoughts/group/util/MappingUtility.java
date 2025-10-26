package com.myreflectionthoughts.group.util;

import com.myreflectionthoughts.group.datamodel.dto.request.CreateDiscussionGroupRequest;
import com.myreflectionthoughts.group.datamodel.dto.response.*;
import com.myreflectionthoughts.group.datamodel.entity.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MappingUtility {

    public DiscussionGroup buildDiscussionGroup(CreateDiscussionGroupRequest createDiscussionGroupRequest){
        DiscussionGroup discussionGroup = new DiscussionGroup();

        discussionGroup.setCreatedAt(String.valueOf(Instant.now().getEpochSecond()));
        discussionGroup.setDescription(createDiscussionGroupRequest.getDescription());
        discussionGroup.setGroupName(createDiscussionGroupRequest.getGroupName());
        discussionGroup.setUsers(new ArrayList<>());
        discussionGroup.setPosts(new ArrayList<>());

        return discussionGroup;
    }

    public List<UserDetailsResponse> buildUserDetailsDTO(List<User> userList){

        return userList.stream().map(user -> {
            UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
            userDetailsResponse.setUserId(user.getUserId());
            userDetailsResponse.setUsername(user.getUsername());
            userDetailsResponse.setRole(user.getRole().name());
            userDetailsResponse.setJoined(user.getJoined());

            return userDetailsResponse;
        }).collect(Collectors.toList());
    }

    public List<PostResponse> buildPostDTO(List<Post> postList){

        return postList.stream().map(user -> {
            PostResponse postResponse = new PostResponse();
            postResponse.setPostId(postResponse.getPostId());
            postResponse.setContent(postResponse.getContent());
            postResponse.setAuthor(new UserDetailsResponse());
            postResponse.setLikes(0);

            return postResponse;
        }).collect(Collectors.toList());
    }


    public DiscussionGroupMetaInfoResponse mapToDTO(DiscussionGroup discussionGroup){

        DiscussionGroupMetaInfoResponse response = new DiscussionGroupMetaInfoResponse();

        response.setCreatedAt(discussionGroup.getCreatedAt());
        response.setDescription(discussionGroup.getDescription());
        response.setGroupName(discussionGroup.getGroupName());
        response.setUsers(buildUserDetailsDTO(discussionGroup.getUsers()));
        response.setGroupId(discussionGroup.getGroupId());

        return response;
    }

    public Post buildPost(String content){
        Post post = new Post();
        post.setPostedAt(String.valueOf(Instant.now()));
        post.setContent(content);
        post.setComments(new ArrayList<>());
        post.setLikes(new ArrayList<>());
        return post;
    }

    public AddPostToGroupResponse buildPostDTO(Post post){
        AddPostToGroupResponse postDTO = new AddPostToGroupResponse();
        postDTO.setPostId(post.getPostId());
        postDTO.setPostedAt(post.getPostedAt());
        postDTO.setContent(post.getContent());
        postDTO.setLikes(post.getLikes().size());
        postDTO.setAuthor(buildUserDetailsDTO(List.of(post.getUser())).get(0));
        return postDTO;
    }

    public AddCommentToPostResponse buildAddCommentToPostResponse(String commentAuthor, Comment comment, Post post){

        AddCommentToPostResponse addCommentToPostResponse = new AddCommentToPostResponse();
        addCommentToPostResponse.setCommentId(comment.getCommentId());
        addCommentToPostResponse.setComment(comment.getCommentText());
        addCommentToPostResponse.setUserId(commentAuthor);
        addCommentToPostResponse.setPostId(post.getPostId());

        return addCommentToPostResponse;
    }

    public <T extends LikePostResponse> T buildLikePostInteractionResponse(Class<T> T, String likeId){

        try {
            T t = T.getDeclaredConstructor().newInstance();
            t.setLikeId(likeId);
            t.setMessage("The like interaction has been registered successfully");
            return t;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }

    }
}
