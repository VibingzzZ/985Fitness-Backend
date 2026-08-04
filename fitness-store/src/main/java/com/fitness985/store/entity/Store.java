package com.fitness985.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 门店实体
 *
 * @author mk
 * @since 2026-08-02
 */
@Data
@TableName(value = "t_985fitness_store", autoResultMap = true)
public class Store {

  @TableId(type = IdType.INPUT)
  private Long id;

  private String name;

  private String shortName;

  private String address;

  private BigDecimal longitude;

  private BigDecimal latitude;

  private String phone;

  private String servicePhone;

  private String wifiName;

  private String wifiPassword;

  @TableField(typeHandler = JacksonTypeHandler.class)
  private List<Object> cover;

  private String intro;

  @TableField(typeHandler = JacksonTypeHandler.class)
  private List<Object> photos;

  private Integer sortOrder;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;

  @TableLogic
  @TableField(select = false)
  private Integer deleted;
}
