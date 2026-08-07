package com.fitness985.fitnesssecurity.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fitness985.fitnesssecurity.LoginPrincipal;
import jakarta.servlet.FilterChain;
import java.time.Duration;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Note: {@link JwtTokenService#parseAccessToken(String)} is declared as a static method, so calls
 * made through an instance reference (as {@link JwtAuthenticationFilter} does) are resolved
 * statically at compile time. A Mockito mock of {@link JwtTokenService} therefore cannot
 * intercept that call, and these tests instead use a real {@link JwtTokenService} configured
 * with a fixed test secret to produce and verify tokens end-to-end.
 */
class JwtAuthenticationFilterTest {

    private static final String TEST_SECRET =
            "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OQ==";

    private JwtTokenService jwtTokenService;
    private JwtAuthenticationFilter filter;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties(TEST_SECRET, Duration.ofHours(2), Duration.ofDays(30));
        jwtTokenService = new JwtTokenService(properties);
        filter = new JwtAuthenticationFilter(jwtTokenService);
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueChainAndSkipAuthenticationWhenHeaderMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldContinueChainAndSkipAuthenticationWhenHeaderIsNotBearer() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateAndPrefixPlainRoleWithRolePrefix() throws Exception {
        LoginPrincipal principal = new LoginPrincipal(1001L, "alice", Set.of("ADMIN"));
        String token = jwtTokenService.createAccessToken(principal);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        filter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(principal);
        assertThat(authentication.getAuthorities())
                .extracting(Object::toString)
                .containsExactly("ROLE_ADMIN");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotDoublePrefixRoleThatAlreadyStartsWithRole() throws Exception {
        LoginPrincipal principal = new LoginPrincipal(1002L, "bob", Set.of("ROLE_USER"));
        String token = jwtTokenService.createAccessToken(principal);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        filter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.getAuthorities())
                .extracting(Object::toString)
                .containsExactly("ROLE_USER");
    }

    @Test
    void shouldClearContextAndContinueChainWhenTokenIsMalformed() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer not-a-valid-jwt");

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldRejectRefreshTokenPresentedAsAccessToken() throws Exception {
        LoginPrincipal principal = new LoginPrincipal(1003L, "carol", Set.of("USER"));
        String refreshToken = jwtTokenService.createRefreshToken(principal);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + refreshToken);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotReparseTokenWhenAlreadyAuthenticated() throws Exception {
        Authentication existing = new TestingAuthenticationToken("already-authenticated", "creds");
        SecurityContextHolder.getContext().setAuthentication(existing);

        LoginPrincipal principal = new LoginPrincipal(1004L, "dave", Set.of("USER"));
        String token = jwtTokenService.createAccessToken(principal);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(existing);
        verify(filterChain).doFilter(request, response);
    }
}