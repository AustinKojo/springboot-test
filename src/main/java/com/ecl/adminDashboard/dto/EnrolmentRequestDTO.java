package com.ecl.adminDashboard.dto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Setter
@Getter
public class EnrolmentRequestDTO {


    private Long customerId;

    private Long productId;

    private String status;

    private Date date;

    private String attachment;

    private String comments;

    private String createdBy;

    private String updatedBy;

}
