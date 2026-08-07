package com.fitnessuser.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPageQueryReq {

    @Min(1)
    private Long pageNo = 1L;

    @Min(1)
    @Max(100)
    private Long pageSize = 20L;

    private String nickname;

    private String phone;

    private Integer status;

    private LocalDateTime registerStartTime;

    private LocalDateTime registerEndTime;

    public long offset() {
        return (pageNo - 1) * pageSize;
    }
}
