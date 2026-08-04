package com.fitness985.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 管理员数据传输对象
 *
 * @author mk
 * @since 2026-08-02
 */
@Data
public class AdminDTO {

  private Long id;

  @NotBlank(message = "用户名不能为空")
  private String username;

  private String password;

  private String realName;

  private String phone;

  private String avatar;

  @NotNull(message = "角色不能为空")
  private Integer role;

  private Long storeId;

  private Integer status;
}
