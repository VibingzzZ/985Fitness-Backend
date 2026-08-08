package com.fitnessuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *  用户表
 */
@Data
@TableName("t_985fitness_user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String openid;

    private String unionid;

    private String nickname;

    private String avatar;

    /**
     * 数据库中保存加密后的手机号。
     */
    private String phone;

    private String phoneHash;

    /**
     * BCrypt密码摘要（微信用户为空，临时密码登录方案）。
     */
    private String password;

    /**
     * 0未知、1男、2女。
     */
    private Integer gender;

    private LocalDate birthday;

    /**
     * 0冻结、1正常、2注销。
     */
    private Integer status;

    private String remark;

    private LocalDateTime registerTime;

    private LocalDateTime lastLoginTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String cancellationReason;

    private LocalDateTime cancellationRequestedAt;

    private LocalDateTime scheduledDeletionAt;

    @TableLogic
    private Integer deleted;
}
