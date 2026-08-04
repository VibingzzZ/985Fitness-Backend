package com.fitness985.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果
 *
 * @author mk
 * @since 2026-08-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

  private Integer code;
  private String message;
  private T data;

  /**
   * 成功响应（带数据）
   *
   * @param data 响应数据
   * @return 响应结果
   */
  public static <T> R<T> ok(T data) {
    return new R<>(200, "success", data);
  }

  /**
   * 成功响应（无数据）
   *
   * @return 响应结果
   */
  public static <T> R<T> ok() {
    return new R<>(200, "success", null);
  }

  /**
   * 错误响应
   *
   * @param message 错误信息
   * @return 响应结果
   */
  public static <T> R<T> error(String message) {
    return new R<>(500, message, null);
  }

  /**
   * 错误响应（带状态码）
   *
   * @param code 状态码
   * @param message 错误信息
   * @return 响应结果
   */
  public static <T> R<T> error(Integer code, String message) {
    return new R<>(code, message, null);
  }
}
