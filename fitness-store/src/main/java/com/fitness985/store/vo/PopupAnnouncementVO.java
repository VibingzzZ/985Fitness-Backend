package com.fitness985.store.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 弹窗公告VO
 *
 * @author mk
 * @since 2026-08-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopupAnnouncementVO {

  private Boolean HasPopup;

  private String AnnouncementId;

  private String Title;

  private String Summary;
}
