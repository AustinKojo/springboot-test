package com.ecl.adminDashboard.dto.analytics;

import com.ecl.adminDashboard.dto.Response;
import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.model.Enrolment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AnalyticsDto extends Response{

    CustomerAnalyticsDto customerAnalyticsDto;

    ProductAnalyticsDto productAnalyticsDto;

    EnrolmentAnalyticsDto enrolmentAnalyticsDto;

    SlaAnalyticsDto slaAnalyticsDto;

    List<CustomerProductCountDto> customerProductCounts;
}
