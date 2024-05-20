package com.ecl.adminDashboard.repository;

import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>{

//    Long countAllBy(String enrolment);
@Query(value = "Select count(id)  from products", nativeQuery = true)
int getProductCount();

    @Query(value = "Select count(state)  from products where state = ?", nativeQuery = true)
    int getProductCountByState(String state);

    List<Product> findByStateNotOrderByCreatedOnDesc(String state);


//    Product findByProductName(String productName);
 Optional<Product> findByProductNameAndState(String productName, String state);

    List<Product> findByState(String state);


}
