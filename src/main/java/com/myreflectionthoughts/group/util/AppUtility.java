package com.myreflectionthoughts.group.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class AppUtility {

    public static String getJsonMessageBody(String filename) throws IOException {
        byte[] content = new ClassPathResource("/response/"+filename+".json").getInputStream().readAllBytes();
        return new String(content);
    }
}
