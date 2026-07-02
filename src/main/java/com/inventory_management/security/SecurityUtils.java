package com.inventory_management.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getCurrentBusinessId() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        if (authentication != null) {
            return (String) authentication.getCredentials();
        }
        throw new RuntimeException("No authenticated business found");
    }

    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        if (authentication != null) {
            return (String) authentication.getPrincipal();
        }
        throw new RuntimeException("No authenticated user found");
    }
}