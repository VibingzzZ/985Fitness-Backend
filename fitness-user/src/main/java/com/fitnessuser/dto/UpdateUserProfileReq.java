package com.fitnessuser.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateUserProfileReq {

    @Size(max = 60, message = "昵称不能超过60个字符")
    private String nickname;

    @Size(max = 500, message = "头像地址不能超过500个字符")
    private String avatar;

    @Min(value = 0, message = "性别参数错误")
    @Max(value = 2, message = "性别参数错误")
    private Integer gender;

    private LocalDate birthday;
}
