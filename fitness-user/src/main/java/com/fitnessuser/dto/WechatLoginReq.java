package com.fitnessuser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录请求体
 */
@Data
public class WechatLoginReq {
    @NotBlank(message = "code不能为空")
    private String code;

    private String nickname;

    private String avatar;

    private String phoneCode;
}
