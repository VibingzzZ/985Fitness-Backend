package com.fitnessuser.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CoachPageQueryReq {
    @Min(1)
    private Long pageNo = 1L;

    @Min(1)
    @Max(100)
    private Long pageSize = 20L;

    private Long storeId;
    private Integer status;
    private String keyword;

    public long offset() {
        return (pageNo - 1) * pageSize;
    }
}
