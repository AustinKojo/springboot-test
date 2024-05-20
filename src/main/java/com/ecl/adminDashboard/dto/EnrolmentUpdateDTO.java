package com.ecl.adminDashboard.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class EnrolmentUpdateDTO {
    private Long id;

    private Long customerId;

    private Long productId;

    private String status;

    private Date date;

    private String attachment;

    private String comments;

    private String createdBy;

    private String updatedBy;

}
