package com.fitnessuser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 密码登录请求体（临时方案，后续微信登录上线后删除）
 */
@Data
public class PasswordLoginReq {
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "密码不能为空")
    private String password;
}