package com.myreflectionthoughts.group.config;

public class RestConstant {

    public static final String API_PREFIX = "/api/group";
    public static final String CREATE_GROUP = "/discussion-groups";
    public static final String READ_GROUP = "/discussion-groups/{groupId}";
    public static final String ADD_USER_TO_GROUP = "/discussion-groups/{groupId}/add-user";
    public static final String READ_USER_OF_A_GROUP = "/discussion-groups/{groupId}/user/{userId}";
    public static final String ADD_POST_IN_GROUP = "/discussion-groups/{groupId}/post";
    public static final String UPDATE_POST_IN_GROUP = "/discussion-groups/{groupId}/post";
    public static final String READ_POSTS_OF_A_GROUP = "/discussion-groups/{groupId}/posts";
    public static final String ADD_COMMENT_TO_POST = "/post/{postId}/comment";
    public static final String READ_COMMENT_OF_A_POST = "/discussion-groups/{groupId}/post/{postId}/comments";
    public static final String LIKE_THE_POST = "/post/{postId}/like";
    public static final String UNLIKE_THE_POST = "/post/{postId}/unlike";
    public static final String LIKE_COMMENT_ON_THE_POST = "post/{postId}/comment/{commentId}/like";
    public static final String UNLIKE_COMMENT_ON_THE_POST = "post/{postId}/comment/{commentId}/unlike";

    public static final String RAISE_REQUEST_TO_JOIN_GROUP = "/join";
}
