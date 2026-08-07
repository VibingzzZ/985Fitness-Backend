package com.fitness985.store.vo;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前门店信息VO
 *
 * @author mk
 * @since 2026-08-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentStoreVO {

  private String StoreId;

  private String Name;

  private String Address;

  private String WifiName;

  private String WifiPassword;

  private String ServicePhone;

  private String BusinessHours;

  private BigDecimal Longitude;

  private BigDecimal Latitude;

  private List<String> StorePhotos;
}
