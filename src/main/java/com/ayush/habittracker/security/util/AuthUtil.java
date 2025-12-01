package com.ayush.habittracker.security.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ayush.habittracker.model.User;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthUtil {
	public User getLoggedInUser() {
	    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    return (User) principal; // since YOU stored User in principal
	}

	public Long getLoggedInUserId() {
	    return getLoggedInUser().getId();
	}
	public String getLoggedInUserRole() {
	    return getLoggedInUser().getRole().name();
	}


    public void checkOwnership(Long loggedInUserId, Long resourceOwnerId) {
    	if(getLoggedInUserRole()=="ADMIN")return;
    	else if (!loggedInUserId.equals(resourceOwnerId) ) {
            throw new RuntimeException("Forbidden: You cannot access another user's resource");
        }
    	return;
    }
}
