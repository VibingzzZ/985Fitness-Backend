package com.fitness985.store.controller;

import com.fitness985.store.service.AnnouncementService;
import com.fitness985.store.vo.AnnouncementDetailVO;
import com.fitness985.store.vo.AnnouncementListVO;
import com.fitness985.store.vo.PopupAnnouncementVO;
import com.fitness985.store.vo.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端公告控制器
 *
 * @author mk
 * @since 2026-08-02
 */
@RestController
@RequestMapping("/client/announcements")
@RequiredArgsConstructor
public class ClientAnnouncementController {

  private final AnnouncementService announcementService;

  /**
   * 店铺公告列表
   *
   * @param storeId 门店ID
   * @param pageNo 页码
   * @param pageSize 每页大小
   * @return 公告列表
   */
  @GetMapping
  public R<AnnouncementListVO> list(
      @RequestParam(required = false) Long storeId,
      @RequestParam(required = false, defaultValue = "1") Integer pageNo,
      @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
    AnnouncementListVO result =
        announcementService.listClientAnnouncements(storeId, pageNo, pageSize);
    return R.ok(result);
  }

  /**
   * 店铺公告详情
   *
   * @param id 公告ID
   * @return 公告详情
   */
  @GetMapping("/{id}")
  public R<AnnouncementDetailVO> detail(@PathVariable Long id) {
    AnnouncementDetailVO detail = announcementService.getAnnouncementDetail(id);
    return R.ok(detail);
  }

  /**
   * 首页强弹公告
   *
   * @param storeId 门店ID
   * @return 弹窗公告
   */
  @GetMapping("/popup")
  public R<PopupAnnouncementVO> popup(@RequestParam(required = false) Long storeId) {
    PopupAnnouncementVO popup = announcementService.getPopupAnnouncement(storeId);
    return R.ok(popup);
  }
}
