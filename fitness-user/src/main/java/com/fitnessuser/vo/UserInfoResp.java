package com.fitnessuser.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserInfoResp {

    private Long id;

    private String nickname;

    private String avatar;

    /**
     * 返回脱敏手机号，例如 138****1234。
     */
    private String phone;

    private Integer gender;

    private LocalDate birthday;

    private Integer status;

    private LocalDateTime registerTime;
}