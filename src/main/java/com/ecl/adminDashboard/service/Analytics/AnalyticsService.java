package com.ecl.adminDashboard.service.Analytics;

import com.ecl.adminDashboard.dto.ResponseObject;
import com.ecl.adminDashboard.dto.ResponseObjectListAnalytics;
import com.ecl.adminDashboard.dto.analytics.*;

import java.util.List;

public interface AnalyticsService {

    public AnalyticsDto getAnalytics();

    List<CustomerProductCountDto> getCustomerProductCounts();

    List<ProductCustomerCountDTO> getProductCustomerCounts();

    List<SlaCustomerProductCountDto> getSlaCustomerProductCounts();

    List<SlaCustomerProductCountDto> getSlaCustomerProductCountsByDateRange(DateRangeRequestDto dateRange);


}
