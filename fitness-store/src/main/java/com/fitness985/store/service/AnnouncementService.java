package com.fitness985.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fitness985.store.entity.Announcement;
import com.fitness985.store.vo.AnnouncementDetailVO;
import com.fitness985.store.vo.AnnouncementListVO;
import com.fitness985.store.vo.PopupAnnouncementVO;

/**
 * 公告服务接口
 *
 * @author mk
 * @since 2026-08-02
 */
public interface AnnouncementService extends IService<Announcement> {

  /**
   * 查询客户端公告列表（分页）
   *
   * @param storeId 门店ID
   * @param pageNo 页码
   * @param pageSize 每页大小
   * @return 公告列表响应
   */
  AnnouncementListVO listClientAnnouncements(Long storeId, Integer pageNo, Integer pageSize);

  /**
   * 获取公告详情（客户端）
   *
   * @param id 公告ID
   * @return 公告详情
   */
  AnnouncementDetailVO getAnnouncementDetail(Long id);

  /**
   * 获取首页弹窗公告
   *
   * @param storeId 门店ID
   * @return 弹窗公告
   */
  PopupAnnouncementVO getPopupAnnouncement(Long storeId);
}
