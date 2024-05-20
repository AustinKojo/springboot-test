package com.ecl.adminDashboard.dto.analytics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CustomerAnalyticsDto {

    private Integer totalCount;

    public CustomerAnalyticsDto(Integer totalCount){


        this.totalCount = totalCount;
    }
}
