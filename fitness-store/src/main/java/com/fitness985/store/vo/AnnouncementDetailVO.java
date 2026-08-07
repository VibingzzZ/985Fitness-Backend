package com.fitness985.store.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公告详情VO
 *
 * @author mk
 * @since 2026-08-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDetailVO {

  private String AnnouncementId;

  private String Title;

  private String Summary;

  private String Content;

  private String Icon;

  private String CreateTime;
}
