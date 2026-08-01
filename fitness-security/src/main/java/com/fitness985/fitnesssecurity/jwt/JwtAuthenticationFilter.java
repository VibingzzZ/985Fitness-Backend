package com.fitness985.fitnesssecurity.jwt;

import com.fitness985.fitnesssecurity.LoginPrincipal;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** 认证过滤器 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** 认证请求头前缀 */
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * 过滤器
     *
     * @param request 请求
     * @param response 响应
     * @param filterChain 过滤器链
     * @throws ServletException 抛出
     * @throws IOException 抛出
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 获取请求头
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 请求头为空或者请求头不是Bearer开头
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorization.substring(BEARER_PREFIX.length());
        try {
            authenticate(token);
        } catch (JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 认证
     *
     * @param token 令牌
     */
    private void authenticate(String token) {
        // 通过认证直接跳过
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }
        // 解析令牌
        LoginPrincipal principal = jwtTokenService.parseAccessToken(token);
        List<SimpleGrantedAuthority> authorities = principal.roles().stream().map(this::toAuthority).toList();
        // 创建认证，是SpringSecurity常用“以认证用户载体”
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null, // 密码
                authorities);

        // 告诉SpringSecurity，当前用户已认证
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private SimpleGrantedAuthority toAuthority(String role) {
        String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return new SimpleGrantedAuthority(authority);
    }
}
