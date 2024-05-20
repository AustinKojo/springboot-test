package com.ecl.adminDashboard.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "enrolment")
public class Enrolment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "customer_id")
    Customer customer;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "product_id")
    Product product;


    @Column(name = "status")
    private String status;

    @Column(name = "due_date")
    private Date date;

    @Column(name = "attachment", length = 3000000)
    private String attachment;

    @Column(name = "comments")
    private String comments;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

}
