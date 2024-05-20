package com.ecl.adminDashboard.dto.analytics;

import com.ecl.adminDashboard.model.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerProductCountDto {
    private String companyName;
    private Long productCount;

    // Constructor, getters, and setters
    public CustomerProductCountDto(String companyName, Long productCount) {
        this.companyName = companyName;
        this.productCount = productCount;
    }

}
