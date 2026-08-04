package com.fitnessuser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BindPhoneReq {

    @NotBlank(message = "微信手机号授权code不能为空")
    private String code;
}
