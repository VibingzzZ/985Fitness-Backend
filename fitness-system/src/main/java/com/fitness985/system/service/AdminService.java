package com.fitness985.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fitness985.system.dto.AdminDTO;
import com.fitness985.system.entity.Admin;
import java.util.List;

/**
 * 管理员服务接口
 *
 * @author mk
 * @since 2026-08-02
 */
public interface AdminService extends IService<Admin> {

  /**
   * 查询管理员列表
   *
   * @param keyword 关键字（可选）
   * @param status 状态（可选）
   * @param role 角色（可选）
   * @return 管理员列表
   */
  List<Admin> listAdmins(String keyword, Integer status, Integer role);

  /**
   * 查询管理员详情
   *
   * @param id 管理员ID
   * @return 管理员信息
   */
  Admin getAdmin(Long id);

  /**
   * 创建管理员
   *
   * @param dto 管理员数据传输对象
   * @return 创建后的管理员信息
   */
  Admin createAdmin(AdminDTO dto);

  /**
   * 更新管理员
   *
   * @param id 管理员ID
   * @param dto 管理员数据传输对象
   * @return 更新后的管理员信息
   */
  Admin updateAdmin(Long id, AdminDTO dto);

  /**
   * 删除管理员
   *
   * @param id 管理员ID
   */
  void deleteAdmin(Long id);

  /**
   * 重置密码
   *
   * @param id 管理员ID
   * @param newPassword 新密码
   */
  void resetPassword(Long id, String newPassword);
}
