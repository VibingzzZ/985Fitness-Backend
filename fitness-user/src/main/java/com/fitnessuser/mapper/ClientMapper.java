package com.fitnessuser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fitnessuser.entity.User;
import com.fitnessuser.vo.ActiveCardResp;
import com.fitnessuser.vo.StoredValueBalanceResp;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClientMapper extends BaseMapper<User> {

    @Select("SELECT * FROM t_985fitness_user WHERE openid = #{openid} AND deleted = 0")
    User findByOpenid(String openid);

    @Select("SELECT COUNT(*) FROM t_985fitness_user WHERE phone = #{phone} AND deleted = 0")
    long countByPhone(String phone);

    @Select("""
            SELECT uc.id AS user_card_id, cp.name, uc.remain_times, uc.expire_time, uc.status
            FROM t_985fitness_user_card uc
            JOIN t_985fitness_card_product cp ON cp.id = uc.card_product_id AND cp.deleted = 0
            WHERE uc.user_id = #{userId} AND uc.status = 1 AND uc.frozen = 0
            ORDER BY uc.expire_time
            """)
    List<ActiveCardResp> findActiveCards(Long userId);

    @Select("SELECT COUNT(*) FROM t_985fitness_user_face WHERE user_id = #{userId} AND status = 1")
    long countActiveFaces(Long userId);

    @Select("""
            SELECT balance, gift_balance, balance + gift_balance AS total_balance,
                   update_time AS updated_at
            FROM t_985fitness_stored_value_account
            WHERE user_id = #{userId}
            """)
    StoredValueBalanceResp findBalance(Long userId);
}
