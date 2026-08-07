package com.fitness985.store.vo;

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

  private Integer Code;

  private String Msg;

  private T Data;

  private String TraceId;

  /**
   * 成功响应
   *
   * @param data 响应数据
   * @return 响应结果
   */
  public static <T> R<T> ok(T data) {
    return new R<>(0, "success", data, generateTraceId());
  }

  /**
   * 错误响应
   *
   * @param msg 错误信息
   * @return 响应结果
   */
  public static <T> R<T> error(String msg) {
    return new R<>(500, msg, null, generateTraceId());
  }

  /**
   * 生成追踪ID
   *
   * @return 追踪ID
   */
  private static String generateTraceId() {
    return "985f-" + System.currentTimeMillis();
  }
}
