package com.fitnessuser.mapper;


import com.fitnessuser.vo.UserInfoResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface ClientMapper  {

    /**
     * 获取用户信息
     * @param userId    用户id
     * @return 返回用户信息
     */
    @Select("SELECT * FROM user_info WHERE user_id = #{userId}")
    UserInfoResp getUserInfo(Long userId);

    /**
     * 获取用户余额
     * @param userId    用户id
     * @return 获取用户余额
     */
    @Select("SELECT balance FROM user_info WHERE user_id = #{userId}")
    String getUserBalance(Long userId);
}
