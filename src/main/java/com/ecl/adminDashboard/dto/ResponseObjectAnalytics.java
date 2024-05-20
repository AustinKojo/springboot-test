package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.dto.analytics.AnalyticsDto;
import com.ecl.adminDashboard.model.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseObjectAnalytics extends Response{
    private AnalyticsDto object;

}
