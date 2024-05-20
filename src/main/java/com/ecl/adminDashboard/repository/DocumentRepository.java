package com.ecl.adminDashboard.repository;

import com.ecl.adminDashboard.model.Documents;
import com.ecl.adminDashboard.model.Enrolment;
import com.ecl.adminDashboard.model.SLA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Documents, Long> {

    List<Documents> findAllByOrderByCreatedOnDesc();

//    List<Documents> findByEnrolment(Enrolment enrolment);

    List<Documents> findBySla(SLA sla);

}
