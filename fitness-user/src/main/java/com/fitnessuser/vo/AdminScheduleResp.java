package com.fitnessuser.vo;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminScheduleResp {
    private Long scheduleId;
    private Long storeId;
    private Long coachId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer capacity;
    private Integer bookedCount;
    private Integer status;
}
