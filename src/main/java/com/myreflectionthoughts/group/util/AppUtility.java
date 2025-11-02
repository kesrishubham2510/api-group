package com.myreflectionthoughts.group.util;

import com.myreflectionthoughts.group.datamodel.entity.UserAuth;
import com.myreflectionthoughts.group.exception.DiscussionGroupException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class AppUtility {

    public static String getJsonMessageBody(String filename) throws IOException {
        byte[] content = new ClassPathResource("/response/"+filename+".json").getInputStream().readAllBytes();
        return new String(content);
    }

    public static String retrieveUserId(){
        String userId = "";

        try {
            UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userId = userAuth.getUserId();
        }catch (Exception ex){
            throw new DiscussionGroupException("INVALID_USER", "Could not retrieve user details from token");
        }

        return userId;
    }
}
