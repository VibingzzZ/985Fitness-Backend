package com.fitnessuser.vo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ActiveCardResp {
    private Long userCardId;
    private String name;
    private Integer remainTimes;
    private LocalDateTime expireTime;
    private Integer status;
}
