package com.fitness985.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fitness985.store.entity.Store;
import com.fitness985.store.vo.CurrentStoreVO;
import com.fitness985.store.vo.NearbyStoreVO;
import java.util.List;

/**
 * 门店服务接口
 *
 * @author mk
 * @since 2026-08-02
 */
public interface StoreService extends IService<Store> {

  /**
   * 查询附近门店列表
   *
   * @param longitude 用户经度
   * @param latitude 用户纬度
   * @return 附近门店列表
   */
  List<NearbyStoreVO> listNearbyStores(Double longitude, Double latitude);

  /**
   * 获取当前门店信息
   *
   * @param storeId 门店ID
   * @return 门店详细信息
   */
  CurrentStoreVO getCurrentStore(Long storeId);
}
