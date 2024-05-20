package com.ecl.adminDashboard.service.Customer;

import com.ecl.adminDashboard.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ecl.adminDashboard.dto.ResponseObject;
import com.ecl.adminDashboard.dto.ResponseObjectList;
import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.repository.CustomerRepository;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Iterator;



@Service
@AllArgsConstructor
public class CustomerImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private SecurityConfig securityConfig;

    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB

    @Override
    public ResponseObject createCustomer(Customer customer) {
        ResponseObject resp = new ResponseObject();

        // Check if the customer with the same companyName already exists
//        Customer existingCustomer = customerRepository.findByCompanyNameAndState(customer.getCompanyName());
        Optional<Customer> existingActiveCustomer = customerRepository.findByCompanyNameAndState(
                customer.getCompanyName(),
                "active"
        );
        if (existingActiveCustomer.isPresent()) {
            // If the customer with the same companyName already exists, return an error response
            resp.setResponseCode("E01");
            resp.setResponseMessage("Customer with the same company name already exists! Failed to create.");
            return resp;
        }

        String createdBy = getCurrentUsername();
        customer.setCreatedBy(createdBy);
        customer.setCreatedOn(LocalDateTime.now());
        customer.setState("active");
        Customer newCustomer = customerRepository.save(customer);
        System.out.println("New customer created with id: "+ newCustomer.getId());


        if (newCustomer.getId() != null) {
//            System.out.println("new id "+ newCustomer.getId() );
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(newCustomer);
        }

        else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed");
        }

        return resp;
    }

    @Override
    public ResponseObjectList getCustomer(String companyName) {
        ResponseObjectList resp = new ResponseObjectList();

        try{
            List<Customer> customers = customerRepository.findByStateNotOrderByCreatedOnDesc("deleted");

            if (!customers.isEmpty()){
                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject(customers);
            }
            else{
                resp.setResponseCode("E01");
                resp.setResponseMessage("Error");
            }
        }
        catch (Exception e){
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to retrieve customers: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public ResponseObject getCustomerById(long id) {

        ResponseObject resp = new ResponseObject();

        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {

            System.out.println("Customer with id "+ customer.get().getId() + " found.");
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(customer.get());
        }

        else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed");
        }

        return resp;
    }

    @Override
    public ResponseObject updateCustomer(long id, Customer updateCustomer) {
        ResponseObject resp = new ResponseObject();


        try{
            String updatedBy = getCurrentUsername();

            Optional<Customer> existingCustomerOptional = customerRepository.findById(id);


            if (existingCustomerOptional.isPresent()){
                Customer existingCustomer = existingCustomerOptional.get();
                System.out.println("existingCustomer = " + updateCustomer);
                existingCustomer.setCompanyName(updateCustomer.getCompanyName());
                existingCustomer.setContactPersonName(updateCustomer.getContactPersonName());
                existingCustomer.setContactPersonEmail(updateCustomer.getContactPersonEmail());
                existingCustomer.setLocation(updateCustomer.getLocation());
                existingCustomer.setContactPersonNumber(updateCustomer.getContactPersonNumber());
                existingCustomer.setWebsite(updateCustomer.getWebsite());
                existingCustomer.setAccountManagerEmail(updateCustomer.getAccountManagerEmail());
                existingCustomer.setUpdatedBy(updatedBy);

                Customer updateCustomerEntity = customerRepository.save(existingCustomer);

                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject(updateCustomerEntity);
            }
            else {
                resp.setResponseCode("E01");
                resp.setResponseMessage("Customer not found for id: " + id);
            }

        }
        catch (Exception e) {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to update customer:" + e.getMessage());
        }

        return resp;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "unknown"; // Default value or handle accordingly
    }

    @Override
    public ResponseObject deleteCustomer(long id) {
        ResponseObject resp = new ResponseObject();
        Optional<Customer> existingCustomer = customerRepository.findById(id);

        if (existingCustomer.isPresent()){
            existingCustomer.get().setState("deleted");
//            customerRepository.deleteById(existingCustomer.get().getId());
            customerRepository.save(existingCustomer.get());
            System.out.println("Customer with id: "+ existingCustomer.get().getId() + " deleted.");
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(existingCustomer.get());
        }

        else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Customer not found for id: " + id);
        }

        return resp;
    }


    @Override
    public ResponseObject uploadCustomersFromExcel(MultipartFile file) {
        ResponseObject resp = new ResponseObject();

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            resp.setResponseCode("E03");
            resp.setResponseMessage("File size exceeds the limit of 10MB.");
            return resp;
        }
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Check if headers match
            Row headerRow = sheet.getRow(0);
            if (!areHeadersMatching(headerRow)) {
                resp.setResponseCode("E02");
                resp.setResponseMessage("Excel file headers do not match the expected format.");
                return resp;
            }

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Customer customer = new Customer();

                // Handle numeric cells
                DataFormatter dataFormatter = new DataFormatter();

                // Extract data into corresponding columns in the database
                customer.setCompanyName(dataFormatter.formatCellValue(row.getCell(0)));
                customer.setContactPersonName(dataFormatter.formatCellValue(row.getCell(1)));
                customer.setContactPersonEmail(dataFormatter.formatCellValue(row.getCell(2)));
                customer.setLocation(dataFormatter.formatCellValue(row.getCell(3)));
                customer.setContactPersonNumber(dataFormatter.formatCellValue(row.getCell(4)));
                customer.setWebsite(dataFormatter.formatCellValue(row.getCell(5)));
                customer.setAccountManagerEmail(dataFormatter.formatCellValue(row.getCell(6)));

                customerRepository.save(customer);
            }

            resp.setResponseCode("000");
            resp.setResponseMessage("Data loaded successfully.");
        } catch (IOException e) {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to update customer: " + e.getMessage());
        }

        return resp;
    }

    private boolean areHeadersMatching(Row headerRow) {
        // Compare the Excel file headers with the expected column names
        List<String> expectedHeaders = Arrays.asList(
                "company_name", "contact_person_name", "contact_person_email",
                "location", "contact_person_number", "website", "account_manager_email"
        );

        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            Cell cell = headerRow.getCell(i);
            String headerCellValue = cell.getStringCellValue().trim().toLowerCase();
            if (!expectedHeaders.get(i).equals(headerCellValue)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public byte[] exportCustomersToExcel() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customers");

        // Example: Add headers to the sheet
        Row headerRow = sheet.createRow(0);
        List<String> headers = Arrays.asList(
                "Company Name", "Contact Person Name", "Contact Person Email",
                "Location", "Contact Person Number", "Website", "Account Manager Email"
        );
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
        }
        // Fetch customer data from the database
        List<Customer> activeCustomers = customerRepository.findByState("active"); // Adjust this based on your repository method

        // Add data rows to the sheet
        for (int rowIndex = 0; rowIndex < activeCustomers.size(); rowIndex++) {
            Customer customer = activeCustomers.get(rowIndex);
            Row dataRow = sheet.createRow(rowIndex + 1); // Add 1 to skip the header row

            dataRow.createCell(0).setCellValue(customer.getCompanyName());
            dataRow.createCell(1).setCellValue(customer.getContactPersonName());
            dataRow.createCell(2).setCellValue(customer.getContactPersonEmail());
            dataRow.createCell(3).setCellValue(customer.getLocation());
            dataRow.createCell(4).setCellValue(customer.getContactPersonNumber());
            dataRow.createCell(5).setCellValue(customer.getWebsite());
            dataRow.createCell(6).setCellValue(customer.getAccountManagerEmail());
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }


//    @Override
//    public ResponseObject findByRenewed() {
//        return null;
//    }

//    @Override
//    public ResponseObject getCustomerCount() {
//        ResponseObject resp = new ResponseObject();
//
//        try {
//            long customerCount = customerRepository.count();
//            resp.setResponseCode("000");
//            resp.setResponseMessage("Success");
//            resp.setObject(customerCount);
//        }
//        catch (Expectation e) {
//            resp.setResponseCode("E01");
//            resp.setResponseMessage("Failed to retrieve customer count:" + e.getMessage());
//        }
//        return resp;
//    }
}