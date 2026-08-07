package com.fitness985.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fitness985.store.entity.Announcement;
import com.fitness985.store.mapper.AnnouncementMapper;
import com.fitness985.store.service.AnnouncementService;
import com.fitness985.store.vo.AnnouncementDetailVO;
import com.fitness985.store.vo.AnnouncementListItemVO;
import com.fitness985.store.vo.AnnouncementListVO;
import com.fitness985.store.vo.PopupAnnouncementVO;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 公告服务实现类
 *
 * @author mk
 * @since 2026-08-02
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement>
    implements AnnouncementService {

  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public AnnouncementListVO listClientAnnouncements(
      Long storeId, Integer pageNo, Integer pageSize) {
    if (pageNo == null || pageNo < 1) {
      pageNo = 1;
    }
    if (pageSize == null || pageSize < 1) {
      pageSize = 20;
    }
    LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
    if (storeId != null) {
      wrapper.eq(Announcement::getStoreId, storeId);
    }
    wrapper.orderByDesc(Announcement::getCreateTime);
    Page<Announcement> page = new Page<>(pageNo, pageSize);
    Page<Announcement> result = page(page, wrapper);
    List<AnnouncementListItemVO> list =
        result.getRecords().stream().map(this::convertToListItemVO).collect(Collectors.toList());
    return new AnnouncementListVO(list, (int) result.getTotal(), pageNo, pageSize);
  }

  @Override
  public AnnouncementDetailVO getAnnouncementDetail(Long id) {
    Announcement announcement = getById(id);
    if (announcement == null) {
      return null;
    }
    return new AnnouncementDetailVO(
        String.valueOf(announcement.getId()),
        announcement.getTitle(),
        announcement.getSummary(),
        announcement.getContent(),
        "NOTICE",
        announcement.getCreateTime() != null
            ? announcement.getCreateTime().format(DATE_FORMATTER)
            : null);
  }

  @Override
  public PopupAnnouncementVO getPopupAnnouncement(Long storeId) {
    LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
    if (storeId != null) {
      wrapper.eq(Announcement::getStoreId, storeId);
    }
    wrapper.orderByDesc(Announcement::getCreateTime);
    wrapper.last("LIMIT 1");
    Announcement announcement = getOne(wrapper);
    if (announcement == null) {
      return new PopupAnnouncementVO(false, null, null, null);
    }
    return new PopupAnnouncementVO(
        true,
        String.valueOf(announcement.getId()),
        announcement.getTitle(),
        announcement.getSummary());
  }

  /**
   * 转换为列表项VO
   *
   * @param announcement 公告实体
   * @return 列表项VO
   */
  private AnnouncementListItemVO convertToListItemVO(Announcement announcement) {
    return new AnnouncementListItemVO(
        String.valueOf(announcement.getId()),
        announcement.getTitle(),
        announcement.getSummary(),
        "NOTICE",
        false,
        announcement.getCreateTime() != null
            ? announcement.getCreateTime().format(DATE_FORMATTER)
            : null);
  }
}
