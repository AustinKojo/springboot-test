package com.ecl.adminDashboard.controller;
import com.ecl.adminDashboard.dto.analytics.*;

import com.ecl.adminDashboard.service.Analytics.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("eclDashboard/api/v1")
@AllArgsConstructor
public class AnalyticsController {
    private AnalyticsService analyticsService;


    @PostMapping(value = "/analytics")
    public AnalyticsDto createCustomer() {

        return analyticsService.getAnalytics();
    }

    @GetMapping("/customer-product-counts")
    public List<CustomerProductCountDto> getCustomerProductCounts() {
        return analyticsService.getCustomerProductCounts();
    }

    @GetMapping("/product-customer-counts")
    public List<ProductCustomerCountDTO> getProductCustomerCounts() {
        return analyticsService.getProductCustomerCounts();
    }

    @GetMapping("/sla-product-customer-counts")
    public List<SlaCustomerProductCountDto> getSlaCustomerProductCounts() {
        return analyticsService.getSlaCustomerProductCounts();
    }

    @PostMapping("/sla-date-range-count")
    public List<SlaCustomerProductCountDto> getSlaCustomerProductCountsByDateRange(@RequestBody DateRangeRequestDto dateRange) {
        return analyticsService.getSlaCustomerProductCountsByDateRange(dateRange);
    }

}
