package com.fitnessuser.vo;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResp<T> {
    private List<T> list;
    private Long pageNo;
    private Long pageSize;
    private Long total;
}
