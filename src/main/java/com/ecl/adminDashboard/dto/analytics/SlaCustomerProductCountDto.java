package com.ecl.adminDashboard.dto.analytics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlaCustomerProductCountDto {
    private String companyName;
    private Long productCount;

    // Constructor, getters, and setters
    public SlaCustomerProductCountDto(String companyName, Long productCount) {
        this.companyName = companyName;
        this.productCount = productCount;
    }
}
