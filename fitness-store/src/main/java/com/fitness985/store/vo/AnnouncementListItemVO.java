package com.fitness985.store.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公告列表项VO
 *
 * @author mk
 * @since 2026-08-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementListItemVO {

  private String AnnouncementId;

  private String Title;

  private String Summary;

  private String Icon;

  private Boolean IsTop;

  private String CreateTime;
}
