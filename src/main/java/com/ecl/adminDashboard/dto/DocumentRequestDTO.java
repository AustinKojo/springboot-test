package com.ecl.adminDashboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DocumentRequestDTO {

    private Long enrolmentId;

    private Long slaId;

    private String documentType;

    private String documentName;

    private String createdBy;

    private String updatedBy;
}
