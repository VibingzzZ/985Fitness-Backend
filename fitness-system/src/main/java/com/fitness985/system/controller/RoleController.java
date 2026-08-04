package com.fitness985.system.controller;

import com.fitness985.system.enums.AdminRole;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色控制器
 *
 * @author mk
 * @since 2026-08-02
 */
@RestController
@RequestMapping("/api/system/roles")
@RequiredArgsConstructor
public class RoleController {

  /**
   * 查询角色列表
   *
   * @return 角色列表
   */
  @GetMapping
  public List<Map<String, Object>> list() {
    return Arrays.stream(AdminRole.values())
        .map(
            role ->
                Map.<String, Object>of(
                    "code", role.getCode(),
                    "name", role.getName()))
        .collect(Collectors.toList());
  }
}
