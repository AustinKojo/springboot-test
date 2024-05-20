package com.ecl.adminDashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_details")
    private String productDetails;

    @Column(name = "state")
    private String state = "active";

    @Column(name = "created_on")
    private String createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    Set<Enrolment> enrolments;

//    @ManyToMany(mappedBy = "products", cascade = { CascadeType.ALL })
//    @ToString.Exclude
//    @JsonIgnore
//    private Set<Customer> employees = new HashSet<Customer>();

}
