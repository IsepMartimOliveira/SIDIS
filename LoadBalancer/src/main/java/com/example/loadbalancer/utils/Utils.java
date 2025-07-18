package com.example.loadbalancer.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

    // Gets the id of current authenticated user
    public static String getAuthId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username.split(",")[0];
    }

    public static String transformSpaces(String input) {
        String trimmed = input.trim();
        String replaced = trimmed.replaceAll("\\s+", " ");
        return replaced.replaceAll(" ", "_");
    }


}
