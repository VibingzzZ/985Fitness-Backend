package com.fitnessuser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 密码注册请求体（临时方案，后续微信登录上线后删除）
 */
@Data
public class PasswordRegisterReq {
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度为6-32位")
    private String password;

    @Size(max = 60, message = "昵称最长60个字符")
    private String nickname;
}
