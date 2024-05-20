package com.ecl.adminDashboard.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "sla")
public class SLA {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "agreement_details")
    private String agreementDetails;

    @Column(name = "status")
    private String status;

    @Column(name = "renewal_date")
    private Date renewalDate;

    @Column(name = "attachment", length = 3000000)
    private String attachment;

    @Column(name = "payment_details")
    private String paymentDetails;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

}
