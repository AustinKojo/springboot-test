package com.ecl.adminDashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "customers")
public class Customer {


    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    @Column(name = "contact_person_email")
    private String contactPersonEmail;

    @Column(name = "location")
    private String location;

    @Column(name = "contact_person_number")
    private String contactPersonNumber;

    @Column(name = "website")
    private String website;

    @Column(name = "account_manager_email")
    private String accountManagerEmail;

    @Column(name = "state")
    private String state = "active";

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;
//    @ToString.Exclude
//    @ManyToMany
//            (cascade = {
//            CascadeType.ALL
//    })
//    @JoinTable(
//            name = "enrolment",
//            joinColumns = {
//                    @JoinColumn(name = "customer_id")
//            },
//            inverseJoinColumns = {
//                    @JoinColumn(name = "product_id")
//            }
//    )
//    Set< Product > products = new HashSet< Product >();

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    Set<Enrolment> enrolments;

}
