package com.fitness985.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 管理员角色枚举
 *
 * @author mk
 * @since 2026-08-02
 */
@Getter
@AllArgsConstructor
public enum AdminRole {
  SUPER_ADMIN(1, "超级管理员"),
  STORE_MANAGER(2, "店长"),
  OPERATOR(3, "运营"),
  FINANCE(4, "财务");

  private final Integer code;
  private final String name;

  /**
   * 根据编码获取角色
   *
   * @param code 角色编码
   * @return 角色枚举
   */
  public static AdminRole fromCode(Integer code) {
    if (code == null) {
      return null;
    }
    for (AdminRole role : values()) {
      if (role.getCode().equals(code)) {
        return role;
      }
    }
    return null;
  }
}
