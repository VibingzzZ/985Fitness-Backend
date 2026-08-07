package com.fitnessuser.vo;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancellationResp {
    private Long userId;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime scheduledDeletionAt;
}
