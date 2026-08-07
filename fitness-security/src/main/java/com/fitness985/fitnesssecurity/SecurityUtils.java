package com.fitness985.fitnesssecurity;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static LoginPrincipal currentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof LoginPrincipal principal)) {
            throw new AuthenticationCredentialsNotFoundException("当前请求未登录");
        }

        return principal;
    }

    public static Long currentUserId() {
        return currentPrincipal().userId();
    }
}
