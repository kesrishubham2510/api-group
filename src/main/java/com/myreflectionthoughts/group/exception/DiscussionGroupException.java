package com.myreflectionthoughts.group.exception;

public class DiscussionGroupException extends RuntimeException{
    private String key;
    private String message;

    public DiscussionGroupException(String key, String message){
        super(message);
        this.key = key;
    }
}
