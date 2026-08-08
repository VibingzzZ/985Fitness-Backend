package com.fitnessuser.vo;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoachResp {
    private Long coachId;
    private Long storeId;
    private String name;
    private String avatar;
    private Integer gender;
    private List<String> specialties;
    private String intro;
    private Long price;
    private Integer status;
}
