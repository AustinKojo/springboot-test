package com.ecl.adminDashboard.repository;

import com.ecl.adminDashboard.model.Enrolment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrolmentRepository extends JpaRepository<Enrolment, Long>{
 //   @Query("SELECT e FROM Enrolment e JOIN FETCH e.status")

    @Query(value = "Select count(id)  from enrolment", nativeQuery = true)
    int getEnrolmentCount();

    @Query(value = "Select count(status)  from enrolment where status = ?", nativeQuery = true)
    int getEnrolmentCountByStatus(String status);

    List<Enrolment> findAllByOrderByCreatedOnDesc();

    @Query(value = "SELECT c.company_name, COUNT(e.product_id) AS product_count " +
            "FROM customers AS c " +
            "INNER JOIN enrolment AS e ON c.id = e.customer_id " +
            "WHERE c.state = 'active'" +
            "GROUP BY c.company_name;", nativeQuery = true)
    List<Object[]> getCustomerProductCount();

    @Query(value = "SELECT c.product_name, COUNT(e.customer_id) AS customer_count " +
            "FROM products AS c " +
            "INNER JOIN enrolment AS e ON c.id = e.product_id " +
            "WHERE c.state = 'active'" +
            "GROUP BY c.product_name;", nativeQuery = true)
    List<Object[]> getProductCustomerCount();

}