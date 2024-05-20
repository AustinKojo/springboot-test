package com.ecl.adminDashboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "documents")
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
//    @JoinColumn(name = "enrolment_id")
//    Enrolment enrolment;

    @JoinColumn(name = "sla_id")
    SLA sla;



    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_name", length = 3000000)
    private String documentName;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

}
