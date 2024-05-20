package com.ecl.adminDashboard.controller;

import com.ecl.adminDashboard.dto.*;
import com.ecl.adminDashboard.model.Enrolment;
import com.ecl.adminDashboard.service.Enrolments.EnrolmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("eclDashboard/api/v1")
@AllArgsConstructor

public class EnrolmentController {
    private EnrolmentService enrolmentService;

    @PostMapping(value = "/enrolments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObjectEnrolment createEnrolment(@RequestBody EnrolmentRequestDTO enrolmentDTO) {
        return enrolmentService.createEnrolment(enrolmentDTO);
    }

    @GetMapping(value = "/enrolments")
    public ResponseObjectListEnrolment getEnrolment (@RequestParam(required = false) String companyName) {
        return enrolmentService.getEnrolment(companyName);
    }

    @GetMapping(value = "/enrolments/{id}")
    public ResponseObjectEnrolment getEnrolmentById (@PathVariable("id") long id) {
        return enrolmentService.getEnrolmentById(id);
    }

    @PutMapping(value = "/enrolments/{id}")
    public ResponseObjectEnrolment updateEnrolment(@PathVariable("id") long id, @RequestBody EnrolmentRequestDTO enrolmentDTO){
        return enrolmentService.updateEnrolment(id, enrolmentDTO);
    }
    @DeleteMapping("/enrolments/{id}")
    public ResponseObjectEnrolment deleteEnrolment(@PathVariable("id") long id){
        return enrolmentService.deleteEnrolment(id);
    }

    @PostMapping("/enrolments/upload")
    public ResponseEntity<ResponseObjectEnrolment> uploadEnrolmentsFromExcel(@RequestParam("file") MultipartFile file) {
        ResponseObjectEnrolment response = enrolmentService.uploadEnrolmentsFromExcel(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/enrolments/download/excel")
    public ResponseEntity<byte[]> downloadProductsAsExcel() {
        byte[] excelBytes = enrolmentService.exportEnrolmentsToExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "enrolments.xlsx");
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

}