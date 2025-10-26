package com.myreflectionthoughts.group.dataprovider.service;

import com.myreflectionthoughts.group.datamodel.dto.request.*;
import com.myreflectionthoughts.group.datamodel.dto.response.AddCommentToPostResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.AddPostToGroupResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.CommentsOnPostResponse;
import com.myreflectionthoughts.group.datamodel.dto.response.LikePostResponse;
import com.myreflectionthoughts.group.datamodel.entity.*;
import com.myreflectionthoughts.group.dataprovider.repository.*;
import com.myreflectionthoughts.group.exception.DiscussionGroupException;
import com.myreflectionthoughts.group.usecase.*;
import com.myreflectionthoughts.group.util.MappingUtility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PostProvider implements
        AddPostToGroup<AddPostToGroupRequest, AddPostToGroupResponse>,
        UpdatePostOfGroup<UpdatePostInAGroup, AddPostToGroupResponse>,
        AddCommentToPost<AddCommentToPostRequest, AddCommentToPostResponse>,
        AddLikeToPost<LikePostRequest, LikePostResponse>,
        RemoveLikeFromPost<UnLikePostRequest, LikePostResponse>,
        AddLikeToComment<LikeCommentOnPostRequest, LikePostResponse>,
        RemoveLikeFromComment<UnlikeCommentOnPostRequest, LikePostResponse>,
        ReadCommentsOnAPost<CommentsOnPostResponse>

{

    private final DiscussionGroupRepository discussionGroupRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MappingUtility mappingUtility;

    public PostProvider(DiscussionGroupRepository discussionGroupRepository,
                                   UserRepository userRepository,
                                   PostRepository postRepository,
                                   CommentRepository commentRepository,
                                   PostLikeRepository postLikeRepository,
                                   CommentLikeRepository commentLikeRepository,
                                   MappingUtility mappingUtility){
        this.discussionGroupRepository = discussionGroupRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.mappingUtility = mappingUtility;
    }

    @Override
    public ResponseEntity<AddPostToGroupResponse> addPostToGroup(AddPostToGroupRequest request) {

        // TODO:- To retrieve the userId from the securityContext of this transaction, once the JWT is implemented

        checkMemberShip(request.getDiscussionGroupId(), request.getAuthorId());

        DiscussionGroup discussionGroup = this.discussionGroupRepository.findById(request.getDiscussionGroupId())
                .orElseThrow(()-> new DiscussionGroupException("INVALID_GROUP_ID", "Discussion group with id:- "+request.getDiscussionGroupId()+", does not exists"));

        User user = userRepository.findById(request.getAuthorId()).orElseThrow(
                ()-> new DiscussionGroupException("INVALID_CREATOR", "User:- "+request.getAuthorId()+", does not exists"));

        Post post = mappingUtility.buildPost(request.getContent());
        post.setUser(user);
        post.setDiscussionGroup(discussionGroup);

        post = postRepository.save(post);

        //TODO:- Analyse if we need to save the post to discussionGroup entity as well

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.status(201).headers(headers).body(mappingUtility.buildPostDTO(post));
    }


    @Override
    public ResponseEntity<AddPostToGroupResponse> updatePost(UpdatePostInAGroup request) {
        // TODO:- To retrieve the userId from the securityContext of this transaction, once the JWT is implemented

        // check membership of the user
        checkMemberShip(request.getDiscussionGroupId(), request.getAuthorId());

        User user = userRepository.findById(request.getAuthorId()).orElseThrow(()->  new DiscussionGroupException("INVALID_CREATOR", "User:- "+request.getAuthorId()+", does not exists"));
        Post post = postRepository.findById(request.getPostId()).orElseThrow(()->  new DiscussionGroupException("INVALID_POST", "Post:- "+request.getPostId()+", does not exists"));

        if(!Objects.equals(post.getUser().getUserId(), user.getUserId()))
            throw new DiscussionGroupException("WRONG_AUTHOR", "User:- "+request.getAuthorId()+" is not the author of the post:-  "+request.getPostId());

        post.setContent(request.getContent());
        post = postRepository.save(post);

        //TODO:- Analyse if we need to save the post to discussionGroup entity as well

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.status(201).headers(headers).body(mappingUtility.buildPostDTO(post));
    }


    @Override
    public ResponseEntity<AddCommentToPostResponse> addComment(AddCommentToPostRequest request) {

        // TODO:- To retrieve the userId from the securityContext of this transaction, once the JWT is implemented

        checkMemberShip(request.getGroupId(), request.getUserId());

        Post post = postRepository.findById(request.getPostId()).orElseThrow(()->  new DiscussionGroupException("INVALID_POST", "Post:- "+request.getPostId()+", does not exists"));

        Comment comment = new Comment();
        comment.setCommentText(request.getComment());
        comment.setCommentedAt(String.valueOf(Instant.now()));
        comment.setLikes(new ArrayList<>());
        comment.setPost(post);
        comment.setUserId(request.getUserId());


        try {
            comment = commentRepository.save(comment);
        }catch (Exception exception){
            throw new DiscussionGroupException("SOMETHING WENT WRONG", "Something went wrong while updating the adding the comment, Please try again");
        }

        HttpHeaders headers = new HttpHeaders();

        return  ResponseEntity.status(201).headers(headers).body(mappingUtility.buildAddCommentToPostResponse(request.getUserId(), comment, post));
    }

    @Override
    public ResponseEntity<LikePostResponse> addLikeToPost(LikePostRequest request) {
        // TODO:- To retrieve the userId from the securityContext of this transaction, once the JWT is implemented

        checkMemberShip(request.getGroupId(), request.getUserId());
        Post post = postRepository.findById(request.getPostId()).orElseThrow(()->  new DiscussionGroupException("INVALID_POST", "Post:- "+request.getPostId()+", does not exists"));

        if(!post.getDiscussionGroup().getGroupId().equalsIgnoreCase(request.getGroupId())){
            throw new DiscussionGroupException("INVALID_POST", "The post:- "+request.getPostId()+", is not linked with the group:- "+request.getGroupId());
        }


        if(!postLikeRepository.findByPostIdAndUserId(request.getPostId(), request.getUserId()).isEmpty()){
            throw new DiscussionGroupException("INTERACTION_ALREADY_REGISTERED", "You can't like a post you have already liked");
        }

        PostLike postLike = new PostLike();

        postLike.setLikedAt(String.valueOf(Instant.now()));
        postLike.setUserId(request.getUserId());
        postLike.setPost(post);

        postLike = postLikeRepository.save(postLike);

        HttpHeaders httpHeaders = new HttpHeaders();

        return ResponseEntity.status(201).headers(httpHeaders).body(mappingUtility.buildLikePostInteractionResponse(LikePostResponse.class, postLike.getLikeId()));
    }

    @Override
    public ResponseEntity<LikePostResponse> removeLikeFromPost(UnLikePostRequest request) {
        // TODO:- To retrieve the userId from the securityContext of this transaction, once the JWT is implemented

        checkMemberShip(request.getGroupId(), request.getUserId());
        Post post = postRepository.findById(request.getPostId()).orElseThrow(()->  new DiscussionGroupException("INVALID_POST", "Post:- "+request.getPostId()+", does not exists"));

        if(!post.getDiscussionGroup().getGroupId().equalsIgnoreCase(request.getGroupId())){
            throw new DiscussionGroupException("INVALID_POST", "The post:- "+request.getPostId()+", is not linked with the group:- "+request.getGroupId());
        }

        if(postLikeRepository.findByPostIdAndUserId(request.getPostId(), request.getUserId()).isEmpty()){
            throw new DiscussionGroupException("INTERACTION_NOT_REGISTERED", "You can't unlike a post you haven't liked");
        }

        postLikeRepository.deleteById(request.getLikeId());

        HttpHeaders httpHeaders = new HttpHeaders();
        return ResponseEntity.status(200).headers(httpHeaders).body(mappingUtility.buildLikePostInteractionResponse(LikePostResponse.class, request.getLikeId()));
    }

    @Override
    public ResponseEntity<LikePostResponse> addLikeToComment(LikeCommentOnPostRequest request) {

        // TODO:- To retrieve the userId from the securityContext of this transaction, once the JWT is implemented
        checkMemberShip(request.getGroupId(), request.getUserId());

        Post post = postRepository.findById(request.getPostId()).orElseThrow(()->  new DiscussionGroupException("INVALID_POST", "Post:- "+request.getPostId()+", does not exists"));

        if(!post.getDiscussionGroup().getGroupId().equalsIgnoreCase(request.getGroupId())){
            throw new DiscussionGroupException("INVALID_POST", "The post:- "+request.getPostId()+", is not linked with the group:- "+request.getGroupId());
        }

        Comment existingComment = commentRepository.findById(request.getCommentId()).orElseThrow(()-> new DiscussionGroupException("INVALID_COMMENT", "The comment:- "+request.getCommentId()+", does not exist"));

        if(commentRepository.findCommentsByPostId(request.getPostId(), request.getCommentId()).isEmpty()){
            throw new DiscussionGroupException("INVALID_COMMENT", "The comment:- "+request.getCommentId()+", is not linked with the post:- "+request.getPostId());
        }

        // check if the user has already liked this comment

        if(!(commentLikeRepository.findByCommentIdAndUserId(request.getCommentId(), request.getUserId())).isEmpty()){
            throw new DiscussionGroupException("INTERACTION_ALREADY_REGISTERED", "You can't like a already liked comment");
        }

        CommentLike commentLike = new CommentLike();
        commentLike.setLikedAt(String.valueOf(Instant.now()));
        commentLike.setUserId(request.getUserId());
        commentLike.setComment(existingComment);

        commentLike =  commentLikeRepository.save(commentLike);

        HttpHeaders httpHeaders = new HttpHeaders();

        return ResponseEntity.status(201).headers(httpHeaders).body(mappingUtility.buildLikePostInteractionResponse(LikePostResponse.class, commentLike.getLikeId()));
    }

    @Override
    public ResponseEntity<LikePostResponse> removeLikeFromComment(UnlikeCommentOnPostRequest request) {

        // TODO:- To retrieve the userId from the securityContext of this transaction, once the JWT is implemented

        checkMemberShip(request.getGroupId(), request.getUserId());

        Post post = postRepository.findById(request.getPostId()).orElseThrow(()->  new DiscussionGroupException("INVALID_POST", "Post:- "+request.getPostId()+", does not exists"));

        if(!post.getDiscussionGroup().getGroupId().equalsIgnoreCase(request.getGroupId())){
            throw new DiscussionGroupException("INVALID_POST", "The post:- "+request.getPostId()+", is not linked with the group:- "+request.getGroupId());
        }

        if(commentRepository.findCommentsByPostId(request.getPostId(), request.getCommentId()).isEmpty()){
            throw new DiscussionGroupException("INVALID_COMMENT", "The comment:- "+request.getCommentId()+", is not linked with the post:- "+request.getPostId());
        }

        // check if the user has already liked this comment

        if(commentLikeRepository.findByCommentIdAndUserId(request.getCommentId(), request.getUserId()).isEmpty()){
            throw new DiscussionGroupException("INTERACTION_NOT_REGISTERED", "You can't unlike a comment you have not liked already");
        }

        commentLikeRepository.deleteById(request.getCommentLikeId());

        HttpHeaders httpHeaders = new HttpHeaders();

        return ResponseEntity.status(201).headers(httpHeaders).body(mappingUtility.buildLikePostInteractionResponse(LikePostResponse.class, request.getCommentLikeId()));
    }

    @Override
    public ResponseEntity<CommentsOnPostResponse> readCommentsOnAPost(String groupId, String postId, String userId) {
        // TODO:- To retrieve the userId from the securityContext of this transaction, once the JWT is implemented

        checkMemberShip(groupId, userId);

        Post post = postRepository.findById(postId).orElseThrow(()->  new DiscussionGroupException("INVALID_POST", "Post:- "+postId+", does not exists"));

        if(!post.getDiscussionGroup().getGroupId().equalsIgnoreCase(groupId)){
            throw new DiscussionGroupException("INVALID_POST", "The post:- "+postId+", is not linked with the group:- "+groupId);
        }

        Pageable pageable  = PageRequest.of(0, 2);
        Page<AddCommentToPostResponse> page = commentRepository.findCommentsByPost_PostId(postId, pageable);
        List<AddCommentToPostResponse> comments = page.stream().collect(Collectors.toCollection(ArrayList::new));

        CommentsOnPostResponse commentsOnPostResponse = new CommentsOnPostResponse();
        commentsOnPostResponse.setPostId(postId);
        commentsOnPostResponse.setComments(comments);

        HttpHeaders httpHeaders = new HttpHeaders();

        return ResponseEntity.status(200).headers(httpHeaders).body(commentsOnPostResponse);
    }

    private void checkMemberShip(String discussionGroupId, String userId){
        if(discussionGroupRepository.findMemberShip(discussionGroupId, userId).isEmpty()){
            throw new DiscussionGroupException("INVALID_REQUEST", "User:- "+userId+", is not a member of the group");
        }

    }
}

