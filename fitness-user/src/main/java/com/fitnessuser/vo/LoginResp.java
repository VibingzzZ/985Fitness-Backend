package com.fitnessuser.vo;

import lombok.Builder;
import lombok.Data;

/**
 *  用户登录响应
 */
@Data
@Builder
public class LoginResp {
    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Long expiresIn;

    private UserInfoResp user;
}
