package com.fitness985.store.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 附近门店VO
 *
 * @author mk
 * @since 2026-08-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NearbyStoreVO {

  private String StoreId;

  private String Name;

  private String Address;

  private Integer DistanceMeter;

  private String ServicePhone;

  private Double Longitude;

  private Double Latitude;
}
