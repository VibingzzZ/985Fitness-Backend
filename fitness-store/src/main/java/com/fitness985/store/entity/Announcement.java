package com.fitness985.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 公告实体
 *
 * @author mk
 * @since 2026-08-02
 */
@Data
@TableName(value = "t_985fitness_announcement", autoResultMap = true)
public class Announcement {

  @TableId(type = IdType.INPUT)
  private Long id;

  private Long storeId;

  private String title;

  private String summary;

  private String content;

  @TableField(typeHandler = JacksonTypeHandler.class)
  private List<Object> images;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;
}
