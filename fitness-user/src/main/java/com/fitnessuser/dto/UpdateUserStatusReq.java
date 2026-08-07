package com.fitnessuser.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserStatusReq {
    @NotNull(message = "用户状态不能为空")
    @Min(value = 0, message = "用户状态错误")
    @Max(value = 2, message = "用户状态错误")
    private Integer status;

    @Size(max = 500, message = "备注不能超过500个字符")
    private String remark;
}
