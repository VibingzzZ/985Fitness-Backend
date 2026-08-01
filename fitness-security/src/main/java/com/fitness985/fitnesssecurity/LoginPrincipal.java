package com.fitness985.fitnesssecurity;

import java.util.Set;

/**
 * 登录用户信息
 */
public record LoginPrincipal(
        Long userId,
        String username,
        Set<String> roles
) {
    public LoginPrincipal {
        roles = roles == null ? Set.of() : Set.copyOf(roles);
    }
}
