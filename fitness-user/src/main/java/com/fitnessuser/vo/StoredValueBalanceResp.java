package com.fitnessuser.vo;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoredValueBalanceResp {
    private Long balance;
    private Long giftBalance;
    private Long totalBalance;
    private LocalDateTime updatedAt;
}
