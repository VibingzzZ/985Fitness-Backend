package com.fitnessuser.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CancellationReq {
    @Size(max = 200, message = "注销原因不能超过200个字符")
    private String reason;
}
