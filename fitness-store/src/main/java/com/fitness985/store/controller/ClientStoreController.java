package com.fitness985.store.controller;

import com.fitness985.store.service.StoreService;
import com.fitness985.store.vo.CurrentStoreVO;
import com.fitness985.store.vo.NearbyStoreVO;
import com.fitness985.store.vo.R;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端门店控制器
 *
 * @author mk
 * @since 2026-08-02
 */
@RestController
@RequestMapping("/client/stores")
@RequiredArgsConstructor
public class ClientStoreController {

  private final StoreService storeService;

    /**
   * 附近门店列表
   *
   * @param longitude 用户当前经度
   * @param latitude 用户当前纬度
   * @return 附近门店列表
   */
  @GetMapping("/nearby")
  public R<List<NearbyStoreVO>> nearby(
      @RequestParam(required = false) Double longitude,
      @RequestParam(required = false) Double latitude) {
    List<NearbyStoreVO> stores = storeService.listNearbyStores(longitude, latitude);
    return R.ok(stores);
  }

  /**
   * 当前门店信息
   *
   * @param storeId 门店ID
   * @return 门店详细信息
   */
  @GetMapping("/current")
  public R<CurrentStoreVO> current(@RequestParam(required = false) Long storeId) {
    CurrentStoreVO store = storeService.getCurrentStore(storeId);
    return R.ok(store);
  }
}
