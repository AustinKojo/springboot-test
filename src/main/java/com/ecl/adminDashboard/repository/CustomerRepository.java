package com.ecl.adminDashboard.repository;

import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository  extends JpaRepository<Customer,Long> {

    @Query(value = "Select count(id)  from customers", nativeQuery = true)
    int getCustomerCount();

    @Query(value = "Select count(state)  from customers where state = ?", nativeQuery = true)
    int getCustomerCountByState(String state);

    List<Customer> findByStateNotOrderByCreatedOnDesc(String state);


//    Customer findByCompanyName(String companyName);

    Optional<Customer> findByCompanyNameAndState(String companyName, String state);

    List<Customer> findByState(String state);



}
