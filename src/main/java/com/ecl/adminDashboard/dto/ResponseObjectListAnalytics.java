package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.dto.analytics.AnalyticsDto;
import com.ecl.adminDashboard.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseObjectListAnalytics extends Response{
    private AnalyticsDto object;
}
