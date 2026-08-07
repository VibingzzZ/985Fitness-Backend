package com.fitness985.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fitness985.store.entity.Store;
import com.fitness985.store.mapper.StoreMapper;
import com.fitness985.store.service.StoreService;
import com.fitness985.store.vo.CurrentStoreVO;
import com.fitness985.store.vo.NearbyStoreVO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 门店服务实现类
 *
 * @author mk
 * @since 2026-08-02
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

  @Override
  public List<NearbyStoreVO> listNearbyStores(Double longitude, Double latitude) {
    List<Store> stores = list(new LambdaQueryWrapper<Store>().orderByDesc(Store::getSortOrder));
    return stores.stream()
        .map(
            store -> {
              double distance = 0;
              if (longitude != null
                  && latitude != null
                  && store.getLatitude() != null
                  && store.getLongitude() != null) {
                distance =
                    calculateDistance(
                        latitude,
                        longitude,
                        store.getLatitude().doubleValue(),
                        store.getLongitude().doubleValue());
              }
              return new NearbyStoreVO(
                  String.valueOf(store.getId()),
                  store.getName(),
                  store.getAddress(),
                  (int) distance,
                  store.getServicePhone(),
                  store.getLongitude() != null ? store.getLongitude().doubleValue() : null,
                  store.getLatitude() != null ? store.getLatitude().doubleValue() : null);
            })
        .sorted((a, b) -> a.getDistanceMeter().compareTo(b.getDistanceMeter()))
        .collect(Collectors.toList());
  }

  @Override
  public CurrentStoreVO getCurrentStore(Long storeId) {
    if (storeId == null) {
      LambdaQueryWrapper<Store> wrapper = new LambdaQueryWrapper<>();
      wrapper.orderByDesc(Store::getSortOrder);
      wrapper.last("LIMIT 1");
      Store store = getOne(wrapper);
      if (store == null) {
        return null;
      }
      storeId = store.getId();
    }
    Store store = getById(storeId);
    if (store == null) {
      return null;
    }
    List<String> photos =
        store.getPhotos() != null
            ? store.getPhotos().stream().map(Object::toString).collect(Collectors.toList())
            : List.of();
    return new CurrentStoreVO(
        String.valueOf(store.getId()),
        store.getName(),
        store.getAddress(),
        store.getWifiName(),
        store.getWifiPassword(),
        store.getServicePhone(),
        "24小时",
        store.getLongitude(),
        store.getLatitude(),
        photos);
  }

  /**
   * 计算两点之间的距离（米）
   *
   * @param lat1 点1纬度
   * @param lon1 点1经度
   * @param lat2 点2纬度
   * @param lon2 点2经度
   * @return 距离（米）
   */
  private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    double earthRadius = 6371000;
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return earthRadius * c;
  }
}
