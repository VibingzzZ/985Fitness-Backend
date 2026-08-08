package com.fitnessuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import java.util.List;
import lombok.Data;

@Data
@TableName(value = "t_985fitness_coach", autoResultMap = true)
public class Coach {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;
    private Long storeId;
    private String name;
    private String avatar;
    private Integer gender;
    private String phone;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> specialties;

    private String intro;
    private Long price;
    private Integer status;
    private Integer deleted;
}
