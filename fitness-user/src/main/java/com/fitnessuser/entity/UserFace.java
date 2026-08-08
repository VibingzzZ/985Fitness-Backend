package com.fitnessuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_985fitness_user_face")
public class UserFace {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;
    private String faceId;
    private String vendor;
    private byte[] featureEnc;
    private String imageUrl;
    private Integer status;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
}
