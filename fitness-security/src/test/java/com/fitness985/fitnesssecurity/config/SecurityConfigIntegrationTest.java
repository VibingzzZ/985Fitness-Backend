package com.fitness985.fitnesssecurity.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fitness985.fitnesssecurity.LoginPrincipal;
import com.fitness985.fitnesssecurity.SecurityExceptionHandler;
import com.fitness985.fitnesssecurity.jwt.JwtAuthenticationFilter;
import com.fitness985.fitnesssecurity.jwt.JwtTokenService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exercises the security rules configured in {@link SecurityConfig} end to end through a minimal
 * test application: permit-all auth endpoints, authenticated endpoints and admin-only endpoints.
 */
@SpringBootTest(classes = SecurityConfigIntegrationTest.TestApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        properties = {
            "security.jwt.secret=MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OQ==",
            "security.jwt.access-token-expiration=2h",
            "security.jwt.refresh-token-expiration=30d"
        })
class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    void permitAllEndpointIsAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/client/auth/login")).andExpect(status().isOk());
    }

    @Test
    void protectedEndpointRejectsAnonymousRequestWithUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/test/secured")).andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpointRejectsRequestWithMalformedToken() throws Exception {
        mockMvc.perform(get("/api/v1/test/secured").header("Authorization", "Bearer garbage-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpointAllowsRequestWithValidAccessToken() throws Exception {
        String token =
                jwtTokenService.createAccessToken(new LoginPrincipal(1L, "alice", Set.of("USER")));

        mockMvc.perform(get("/api/v1/test/secured").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void adminEndpointRejectsUserWithoutAdminRole() throws Exception {
        String token =
                jwtTokenService.createAccessToken(new LoginPrincipal(2L, "bob", Set.of("USER")));

        mockMvc.perform(get("/api/v1/admin/test").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpointAllowsUserWithAdminRole() throws Exception {
        String token =
                jwtTokenService.createAccessToken(new LoginPrincipal(3L, "carol", Set.of("ADMIN")));

        mockMvc.perform(get("/api/v1/admin/test").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Configuration
    @EnableAutoConfiguration
    @Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        SecurityExceptionHandler.class,
        JwtTokenService.class,
        TestApplication.TestController.class
    })
    static class TestApplication {

        @RestController
        static class TestController {

            @GetMapping("/api/v1/client/auth/login")
            String login() {
                return "ok";
            }

            @GetMapping("/api/v1/test/secured")
            String secured() {
                return "ok";
            }

            @GetMapping("/api/v1/admin/test")
            String admin() {
                return "ok";
            }
        }
    }
}