package com.fitnessuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_985fitness_schedule")
public class Schedule {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long storeId;
    private Long coachId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer capacity;
    private Integer bookedCount;
    private Integer status;

    @TableLogic
    private Integer deleted;
}
