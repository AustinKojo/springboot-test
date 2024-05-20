package com.ecl.adminDashboard.dto.analytics;
import com.ecl.adminDashboard.model.Customer;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ProductCustomerCountDTO {
    private String productName;
    private Long companyCount;

    public ProductCustomerCountDTO(String productName, Long companyCount) {
        this.productName = productName;
        this.companyCount = companyCount;
    }
}

