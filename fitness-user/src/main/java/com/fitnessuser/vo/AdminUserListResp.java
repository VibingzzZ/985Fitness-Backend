package com.fitnessuser.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员后台查看用户列表响应
 */
@Data
public class AdminUserListResp {

    private Long userId;

    private String nickname;

    private String avatar;

    private String phoneMasked;

    private Integer gender;

    private Integer status;

    private LocalDateTime registerTime;

    private LocalDateTime lastLoginTime;
}
