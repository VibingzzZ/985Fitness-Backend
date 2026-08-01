package com.fitness985.fitnesssecurity.jwt;

import com.fitness985.fitnesssecurity.LoginPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

/**
 *      生成Token
 *     验签
 *     解析Claims
 **/
@Service
public class JwtTokenService {
    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";

    private static final String ACCESS_TOKEN = "access";
    private static final String REFRESH_TOKEN = "refresh";

    private final JwtProperties properties;
    private final SecretKey signingKey;

    /**
     * 构造函数
     * @param properties 配置
     */
    public JwtTokenService(JwtProperties properties){
        this.properties=properties;
        byte[] keyBytes = Decoders.BASE64.decode(
                properties.secret().replace('-', '+').replace('_', '/'));

        this.signingKey= Keys.hmacShaKeyFor(keyBytes);
    }
    /**
     * 创建AcessToken
     * @param principal 登录用户信息
     * @return 访问令牌
     */
    public String createAccessToken(LoginPrincipal principal) {
        return createToken(
                principal,
                ACCESS_TOKEN,
                properties.accessTokenExpiration()
        );
    }

    /**
     * 创建RefreshToken
     * @param principal 登录用户信息
     * @return 刷新令牌
     */
    public String createRefreshToken(LoginPrincipal principal) {
        return createToken(
                principal,
                REFRESH_TOKEN,
                properties.refreshTokenExpiration()
        );
    }

    /**
     * 解析令牌获取用户信息
     * @param token 令牌
     * @return 用户信息
     */
    public LoginPrincipal parseAccessToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        checkTokenType(claims, ACCESS_TOKEN);
        return toPrincipal(claims);
    }


    /**
     * 解析令牌获取用户信息
     * @param token 令牌
     * @return 用户信息
     */
    public LoginPrincipal parseRefreshToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        checkTokenType(claims, REFRESH_TOKEN);
        return toPrincipal(claims);
    }

    /**
     * 创建令牌
     * @param principal 登录用户信息
     * @param tokenType 令牌类型
     * @param expiration 过期时间
     * @return 令牌
     */
    private String createToken(
            LoginPrincipal principal,
            String tokenType,
            Duration expiration) {

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(principal.userId().toString())
                .claim(CLAIM_USERNAME, principal.username())
                .claim(CLAIM_ROLES, principal.roles())
                .claim(CLAIM_TOKEN_TYPE, tokenType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }


    /**
     * 转换为用户信息
     * @param claims 令牌信息
     * @return 用户信息
     */
    private LoginPrincipal toPrincipal(Claims claims) {
        Long userId = Long.valueOf(claims.getSubject());
        String username = claims.get(CLAIM_USERNAME, String.class);
        Object rawRoles = claims.get(CLAIM_ROLES);
        Set<String> roles = rawRoles instanceof Collection<?> collection
                ? collection.stream().map(String::valueOf).collect(Collectors.toUnmodifiableSet())
                : Set.of();
        return new LoginPrincipal(userId, username, roles);
    }

    /**
     * 校验令牌类型
     * @param claims 令牌信息
     * @param expectedType 令牌类型
     */
    private void checkTokenType(Claims claims, String expectedType) {
        String actualType = claims.get(CLAIM_TOKEN_TYPE, String.class);

        if (!Objects.equals(actualType, expectedType)) {
            throw new JwtException("Token 类型不正确");
        }
    }
}
