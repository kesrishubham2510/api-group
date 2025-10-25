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

    public List<UserDetailsDTO> buildUserDetailsDTO(List<User> userList){

        return userList.stream().map(user -> {
            UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
            userDetailsDTO.setUserId(user.getUserId());
            userDetailsDTO.setUsername(user.getUsername());
            userDetailsDTO.setRole(user.getRole().name());
            userDetailsDTO.setJoined(user.getJoined());

            return userDetailsDTO;
        }).collect(Collectors.toList());
    }

    public List<PostDTO> buildPostDTO(List<Post> postList){

        return postList.stream().map(user -> {
            PostDTO postDTO = new PostDTO();
            postDTO.setPostId(postDTO.getPostId());
            postDTO.setContent(postDTO.getContent());
            postDTO.setAuthor(new UserDetailsDTO());
            postDTO.setLikes(0);

            return postDTO;
        }).collect(Collectors.toList());
    }


    public DiscussionGroupMetaInfoDTO mapToDTO(DiscussionGroup discussionGroup){

        DiscussionGroupMetaInfoDTO response = new DiscussionGroupMetaInfoDTO();

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
