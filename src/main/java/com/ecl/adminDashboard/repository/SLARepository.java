package com.ecl.adminDashboard.repository;

import com.ecl.adminDashboard.model.SLA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public interface SLARepository extends JpaRepository<SLA, Long>{

    @Query(value = "Select count(id)  from sla", nativeQuery = true)
    int getSLACount();

    @Query(value = "Select count(status)  from sla where status = ?", nativeQuery = true)
    int getSLACountByStatus(String status);

    List<SLA> findAllByOrderByCreatedOnDesc();

    @Query(value = "SELECT company_name, COUNT(agreement_details) AS sla_count FROM sla GROUP BY company_name", nativeQuery = true)
    List<Object[]> getSlaCustomerProductCount();

//    @Query(value = "Select * from sla WHERE created_on BETWEEN '2023-12-21' AND '2024-03-26'", nativeQuery = true)
//    int getSlaRange(String state);

    @Query(value = "SELECT company_name, COUNT(agreement_details) AS " +
                   "sla_count FROM sla  WHERE created_on BETWEEN :startDate AND :endDate " +
                   "GROUP BY company_name;", nativeQuery = true)
    List<Object[]> getSlaCustomerProductCountByDateRange(
            @Param("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @Param("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate);



}
