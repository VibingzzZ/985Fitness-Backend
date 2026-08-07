package com.fitness985.fitnesssecurity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

class SecurityUtilsTest {

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void currentPrincipalShouldReturnLoginPrincipalWhenAuthenticated() {
        LoginPrincipal principal = new LoginPrincipal(1001L, "alice", Set.of("USER"));
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(principal, null, Set.of()));

        LoginPrincipal result = SecurityUtils.currentPrincipal();

        assertThat(result).isEqualTo(principal);
    }

    @Test
    void currentPrincipalShouldThrowWhenNoAuthenticationPresent() {
        assertThatThrownBy(SecurityUtils::currentPrincipal)
                .isInstanceOf(AuthenticationCredentialsNotFoundException.class);
    }

    @Test
    void currentPrincipalShouldThrowWhenPrincipalIsNotLoginPrincipal() {
        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken("not-a-login-principal", "creds"));

        assertThatThrownBy(SecurityUtils::currentPrincipal)
                .isInstanceOf(AuthenticationCredentialsNotFoundException.class);
    }

    @Test
    void currentUserIdShouldReturnUserIdFromPrincipal() {
        LoginPrincipal principal = new LoginPrincipal(2002L, "bob", Set.of("ADMIN"));
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(principal, null, Set.of()));

        Long userId = SecurityUtils.currentUserId();

        assertThat(userId).isEqualTo(2002L);
    }

    @Test
    void currentUserIdShouldThrowWhenNotAuthenticated() {
        assertThatThrownBy(SecurityUtils::currentUserId)
                .isInstanceOf(AuthenticationCredentialsNotFoundException.class);
    }
}