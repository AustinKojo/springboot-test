package com.ecl.adminDashboard.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class SlaRequestDTO {

    private String companyName;

    private String agreementDetails;

    private Date renewalDate;

    private String attachment;

    private String paymentDetails;

    private String createdBy;

    private String updatedBy;
}
