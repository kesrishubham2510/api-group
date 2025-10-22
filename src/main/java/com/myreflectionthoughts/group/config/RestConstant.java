package com.myreflectionthoughts.group.config;

public class RestConstant {

    public static final String API_PREFIX = "/api/group";
    public static final String CREATE_GROUP = "/discussion-groups";
    public static final String READ_GROUP = "/discussion-groups/{groupId}";
    public static final String ADD_USER_TO_GROUP = "/discussion-groups/{groupId}/add-user";
    public static final String READ_USER_OF_A_GROUP = "/discussion-groups/{groupId}/user/{userId}";
    public static final String ADD_POST_IN_GROUP = "/discussion-groups/{groupId}/post";

    public static final String UPDATE_POST_IN_GROUP = "/discussion-groups/{groupId}/post";

    public static final String GET_POSTS_OF_A_GROUP = "/discussion-groups/{groupId}/";

}
