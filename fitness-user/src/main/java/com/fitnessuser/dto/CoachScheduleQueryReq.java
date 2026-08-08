package com.fitnessuser.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class CoachScheduleQueryReq {
    @NotNull(message = "coachId不能为空")
    private Long coachId;

    @NotNull(message = "date不能为空")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private Long storeId;
}
