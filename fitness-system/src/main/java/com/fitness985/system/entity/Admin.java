package com.fitness985.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 管理员实体
 *
 * @author mk
 * @since 2026-08-02
 */
@Data
@TableName("t_985fitness_admin")
public class Admin {

  @TableId(type = IdType.INPUT)
  private Long id;

  private String username;

  private String password;

  private String realName;

  private String phone;

  private String avatar;

  private Integer role;

  private Long storeId;

  private Integer status;

  private String lastLoginIp;

  private LocalDateTime lastLoginTime;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;

  @TableLogic
  @TableField(select = false)
  private Integer deleted;
}
