package com.ecl.adminDashboard.repository;
import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.model.SLA;
import com.ecl.adminDashboard.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsernameAndPassword(String username, String password);
    Users findByUsername(String username);
//    @Query(value = "SELECT email FROM users WHERE role = 'Admin'", nativeQuery = true)
    List<Users> findByRole(String role);

    List<Users> findAllByOrderByCreatedOnDesc();






}
