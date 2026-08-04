package com.fitness985.store.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公告列表响应VO
 *
 * @author mk
 * @since 2026-08-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementListVO {

  private List<AnnouncementListItemVO> List;

  private Integer Total;

  private Integer PageNo;

  private Integer PageSize;
}
