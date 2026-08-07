package com.fitness985.fitnesssecurity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

class SecurityExceptionHandlerTest {

    private final SecurityExceptionHandler handler = new SecurityExceptionHandler();
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void commenceShouldSendUnauthorizedStatusWithMessage() throws Exception {
        handler.commence(request, response, new BadCredentialsException("bad credentials"));

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getErrorMessage()).isEqualTo("未登录或登录已过期");
    }

    @Test
    void handleShouldSendForbiddenStatusWithMessage() throws Exception {
        handler.handle(request, response, new AccessDeniedException("denied"));

        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getErrorMessage()).isEqualTo("没有访问权限");
    }

    @Test
    void commenceMessageShouldNotDependOnExceptionContent() throws Exception {
        handler.commence(request, response, new BadCredentialsException("irrelevant detail"));

        assertThat(response.getErrorMessage()).isEqualTo("未登录或登录已过期");
    }
}