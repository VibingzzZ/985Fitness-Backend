package com.fitnessuser.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateIserProfileReq {

    @Size(max = 60, message = "昵称不能超过60个字符")
    private String nickname;

    private String avatar;

    @Min(value = 0, message = "性别参数错误")
    @Max(value = 2, message = "性别参数错误")
    private Integer gender;

    private LocalDate birthday;

}
