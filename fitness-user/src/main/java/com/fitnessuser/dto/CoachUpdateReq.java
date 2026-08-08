package com.fitnessuser.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class CoachUpdateReq {
    @NotBlank
    @Size(max = 64)
    private String name;

    @Size(max = 500)
    private String avatar;

    @Min(0)
    @Max(2)
    private Integer gender;

    private List<@Size(max = 32) String> specialties;

    @Size(max = 1000)
    private String intro;

    @PositiveOrZero
    private Long price;

    @Min(0)
    @Max(2)
    private Integer status;

    private Long storeId;
}
