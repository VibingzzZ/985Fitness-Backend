package com.fitnessuser.mapper;

import com.fitnessuser.dto.UserPageQueryReq;
import com.fitnessuser.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserManageMapper {

    @Select("""
            <script>
            SELECT * FROM t_985fitness_user
            WHERE deleted = 0
            <if test='nickname != null and nickname != ""'>
              AND nickname LIKE CONCAT('%', #{nickname}, '%')
            </if>
            <if test='phone != null and phone != ""'>
              AND (phone_hash = #{phoneHash} OR (phone_hash IS NULL AND phone = #{phone}))
            </if>
            <if test='status != null'>AND status = #{status}</if>
            <if test='registerStartTime != null'>AND register_time &gt;= #{registerStartTime}</if>
            <if test='registerEndTime != null'>AND register_time &lt;= #{registerEndTime}</if>
            ORDER BY register_time DESC
            LIMIT #{pageSize} OFFSET #{offset}
            </script>
            """)
    List<User> findPage(UserPageQueryReq request);

    @Select("""
            <script>
            SELECT COUNT(*) FROM t_985fitness_user
            WHERE deleted = 0
            <if test='nickname != null and nickname != ""'>
              AND nickname LIKE CONCAT('%', #{nickname}, '%')
            </if>
            <if test='phone != null and phone != ""'>
              AND (phone_hash = #{phoneHash} OR (phone_hash IS NULL AND phone = #{phone}))
            </if>
            <if test='status != null'>AND status = #{status}</if>
            <if test='registerStartTime != null'>AND register_time &gt;= #{registerStartTime}</if>
            <if test='registerEndTime != null'>AND register_time &lt;= #{registerEndTime}</if>
            </script>
            """)
    long count(UserPageQueryReq request);
}
