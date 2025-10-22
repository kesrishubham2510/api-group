package com.myreflectionthoughts.group.datamodel.role;

public enum UserRole {

    USER("a user registering on the website"),
    ADMIN("a user promoted by Master"),
    MASTER("comes pre-configured");

    UserRole(String description){
        this.roleDescription = description;
    }

    private final String roleDescription;

    @Override
    public String toString(){
        return this.name()+":- "+this.roleDescription;
    }
}
