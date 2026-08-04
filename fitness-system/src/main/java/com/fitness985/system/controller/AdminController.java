package com.fitness985.system.controller;

import com.fitness985.system.dto.AdminDTO;
import com.fitness985.system.entity.Admin;
import com.fitness985.system.service.AdminService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员控制器
 *
 * @author mk
 * @since 2026-08-02
 */
@RestController
@RequestMapping("/api/system/admins")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  /**
   * 查询管理员列表
   *
   * @param keyword 关键字（可选）
   * @param status 状态（可选）
   * @param role 角色（可选）
   * @return 管理员列表
   */
  @GetMapping
  public List<Admin> list(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Integer status,
      @RequestParam(required = false) Integer role) {
    return adminService.listAdmins(keyword, status, role);
  }

  /**
   * 查询管理员详情
   *
   * @param id 管理员ID
   * @return 管理员信息
   */
  @GetMapping("/{id}")
  public Admin get(@PathVariable Long id) {
    return adminService.getAdmin(id);
  }

  /**
   * 创建管理员
   *
   * @param dto 管理员数据传输对象
   * @return 创建后的管理员信息
   */
  @PostMapping
  public Admin create(@Valid @RequestBody AdminDTO dto) {
    return adminService.createAdmin(dto);
  }

  /**
   * 更新管理员
   *
   * @param id 管理员ID
   * @param dto 管理员数据传输对象
   * @return 更新后的管理员信息
   */
  @PutMapping("/{id}")
  public Admin update(@PathVariable Long id, @Valid @RequestBody AdminDTO dto) {
    return adminService.updateAdmin(id, dto);
  }

  /**
   * 删除管理员
   *
   * @param id 管理员ID
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    adminService.deleteAdmin(id);
  }

  /**
   * 重置密码
   *
   * @param id 管理员ID
   * @param newPassword 新密码
   */
  @PostMapping("/{id}/reset-password")
  public void resetPassword(@PathVariable Long id, @RequestBody String newPassword) {
    adminService.resetPassword(id, newPassword);
  }
}
