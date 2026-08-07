package com.fitness985.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fitness985.system.dto.AdminDTO;
import com.fitness985.system.entity.Admin;
import com.fitness985.system.mapper.AdminMapper;
import com.fitness985.system.service.AdminService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 管理员服务实现类
 *
 * @author mk
 * @since 2026-08-02
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

  @Override
  public List<Admin> listAdmins(String keyword, Integer status, Integer role) {
    LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
    if (StringUtils.hasText(keyword)) {
      wrapper.and(
          w ->
              w.like(Admin::getUsername, keyword)
                  .or()
                  .like(Admin::getRealName, keyword)
                  .or()
                  .like(Admin::getPhone, keyword));
    }
    if (status != null) {
      wrapper.eq(Admin::getStatus, status);
    }
    if (role != null) {
      wrapper.eq(Admin::getRole, role);
    }
    wrapper.orderByDesc(Admin::getCreateTime);
    return list(wrapper);
  }

  @Override
  public Admin getAdmin(Long id) {
    return getById(id);
  }

  @Override
  public Admin createAdmin(AdminDTO dto) {
    Admin admin = convertToEntity(dto);
    if (admin.getId() == null) {
      admin.setId(generateSnowflakeId());
    }
    if (admin.getStatus() == null) {
      admin.setStatus(1);
    }
    save(admin);
    return admin;
  }

  @Override
  public Admin updateAdmin(Long id, AdminDTO dto) {
    Admin exists = getById(id);
    if (exists == null) {
      throw new IllegalArgumentException("管理员不存在: " + id);
    }
    Admin admin = convertToEntity(dto);
    admin.setId(id);
    updateById(admin);
    return getById(id);
  }

  @Override
  public void deleteAdmin(Long id) {
    removeById(id);
  }

  @Override
  public void resetPassword(Long id, String newPassword) {
    Admin admin = getById(id);
    if (admin == null) {
      throw new IllegalArgumentException("管理员不存在: " + id);
    }
    admin.setPassword(newPassword);
    updateById(admin);
  }

  /**
   * 将DTO转换为实体
   *
   * @param dto 管理员数据传输对象
   * @return 管理员实体
   */
  private Admin convertToEntity(AdminDTO dto) {
    Admin admin = new Admin();
    admin.setId(dto.getId());
    admin.setUsername(dto.getUsername());
    admin.setPassword(dto.getPassword());
    admin.setRealName(dto.getRealName());
    admin.setPhone(dto.getPhone());
    admin.setAvatar(dto.getAvatar());
    admin.setRole(dto.getRole());
    admin.setStoreId(dto.getStoreId());
    admin.setStatus(dto.getStatus());
    return admin;
  }

  /**
   * 生成雪花算法ID
   *
   * @return 唯一ID
   */
  private Long generateSnowflakeId() {
    return System.currentTimeMillis() * 1000 + Math.abs(UUID.randomUUID().hashCode() % 1000);
  }
}
