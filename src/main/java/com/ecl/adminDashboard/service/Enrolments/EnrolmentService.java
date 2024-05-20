package com.ecl.adminDashboard.service.Enrolments;

import com.ecl.adminDashboard.dto.EnrolmentRequestDTO;
import com.ecl.adminDashboard.dto.ResponseObjectEnrolment;
import com.ecl.adminDashboard.dto.ResponseObjectListEnrolment;
import com.ecl.adminDashboard.dto.ResponseObjectProduct;
import com.ecl.adminDashboard.model.Enrolment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public interface EnrolmentService {

    public ResponseObjectEnrolment createEnrolment(EnrolmentRequestDTO enrolment);

    public ResponseObjectListEnrolment getEnrolment(String customerId);

    public ResponseObjectEnrolment getEnrolmentById (long id);

    public ResponseObjectEnrolment updateEnrolment (long id, EnrolmentRequestDTO updateEnrolmentDTO);

    public ResponseObjectEnrolment deleteEnrolment (long id);

    public ResponseObjectEnrolment uploadEnrolmentsFromExcel(MultipartFile file);

    byte[] exportEnrolmentsToExcel();

}
