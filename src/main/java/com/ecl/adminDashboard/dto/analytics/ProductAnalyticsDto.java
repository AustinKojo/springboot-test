package com.ecl.adminDashboard.dto.analytics;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductAnalyticsDto {
    private Integer productCount;

    public ProductAnalyticsDto(Integer productCount){

        this.productCount = productCount;
    }

}