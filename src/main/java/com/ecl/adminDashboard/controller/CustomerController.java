package com.ecl.adminDashboard.controller;

import com.ecl.adminDashboard.dto.ResponseObject;
import com.ecl.adminDashboard.dto.ResponseObjectList;
import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.service.Customer.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("${apiBaseUrl}")
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;

    @PostMapping(value = "/customers")
    public ResponseObject createCustomer (@RequestBody Customer customer){
       return  customerService.createCustomer(customer);
    }

    @GetMapping(value = "/customers")
    public ResponseObjectList getCustomer (@RequestParam(required = false) String companyName) {
        return  customerService.getCustomer(companyName);
    }

    @GetMapping(value = "/customers/{id}")
    public ResponseObject getCustomerById (@PathVariable("id") long id) {
        return  customerService.getCustomerById(id);
    }

    @PutMapping("/customers/{id}")
    public ResponseObject updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
        return  customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseObject deleteCustomer(@PathVariable("id") long id) {
        return  customerService.deleteCustomer(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseObject> uploadCustomersFromExcel(@RequestParam("file") MultipartFile file) {
        ResponseObject response = customerService.uploadCustomersFromExcel(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/excel")
    public ResponseEntity<byte[]> downloadCustomersAsExcel() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        byte[] excelBytes = customerService.exportCustomersToExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "customertable"+currentDate+".xlsx");
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

}
