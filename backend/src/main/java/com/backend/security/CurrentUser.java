package com.backend.security;

import com.backend.exception.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUser {

    public UUID id(Authentication authentication){
        if (authentication ==null || !(authentication.getPrincipal() instanceof ChatPrincipal principal)){
            throw new ForbiddenException("Authentication is requited");
        }
        return principal.userId();
    }
}
