package com.ecl.adminDashboard.service.Enrolments;

import com.ecl.adminDashboard.dto.*;
import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.model.Enrolment;
import com.ecl.adminDashboard.model.Product;
import com.ecl.adminDashboard.repository.CustomerRepository;
import com.ecl.adminDashboard.repository.EnrolmentRepository;

import com.ecl.adminDashboard.repository.ProductRepository;
import com.ecl.adminDashboard.service.Email.SendNewEnrolment;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class EnrolmentImpl implements EnrolmentService{
    private EnrolmentRepository enrolmentRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB

    @Autowired
    private SendNewEnrolment sendNewEnrolment;

@Override
public ResponseObjectEnrolment createEnrolment(EnrolmentRequestDTO enrolmentDTO) {
    ResponseObjectEnrolment resp = new ResponseObjectEnrolment();
    Enrolment enrolment = new Enrolment();

    // Fetch Customer and Product entities based on the provided IDs
    Customer customer = customerRepository.findById(enrolmentDTO.getCustomerId()).orElse(null);
    Product product = productRepository.findById(enrolmentDTO.getProductId()).orElse(null);

    // Check if both Customer and Product are found
    if (customer != null && product != null) {
        enrolment.setCustomer(customer);
        enrolment.setProduct(product);

//        enrolment.setStatus(enrolmentDTO.getStatus());
        enrolment.setDate(enrolmentDTO.getDate());
        enrolment.setAttachment(enrolmentDTO.getAttachment());
        enrolment.setCreatedOn(LocalDateTime.now());


        if (enrolment.getDate() != null) {
            Date currentDate = new Date();
            if (enrolment.getDate().compareTo(currentDate) < 0) {
                enrolment.setStatus("EXPIRED");
            } else {
                enrolment.setStatus("ACTIVE");
            }
        }

        enrolment.setComments(enrolmentDTO.getComments());

        Enrolment newEnrolment = enrolmentRepository.save(enrolment);


        sendNewEnrolment.sendEnrolmentCreatedEmail(newEnrolment);

        resp.setResponseCode("000");
        resp.setResponseMessage("Success");
        resp.setObject(newEnrolment);
    } else {
        resp.setResponseCode("E01");
        resp.setResponseMessage("Customer or Product not found");
    }

    return resp;
}




    @Override
    public ResponseObjectListEnrolment getEnrolment(String companyName){
        ResponseObjectListEnrolment resp = new ResponseObjectListEnrolment();

        try {
            List<Enrolment> enrolments = enrolmentRepository.findAllByOrderByCreatedOnDesc();

            if (!enrolments.isEmpty()){
                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject(enrolments);
            }
            else {
                resp.setResponseCode("E01");
                resp.setResponseMessage("Error");
            }
        }
        catch (Exception e){
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to retrieve enrolments: " + e.getMessage());
        }
        return  resp;
    }

    @Override
    public ResponseObjectEnrolment getEnrolmentById(long id) {
        ResponseObjectEnrolment resp = new ResponseObjectEnrolment();

        Optional<Enrolment> enrolment = enrolmentRepository.findById(id);

        if (enrolment.isPresent()) {
            System.out.println("id found " + enrolment.get().getId());
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(enrolment.get());
        }
        else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed");
        }
        return resp;
    }

//    @Override
//    public ResponseObjectEnrolment updateEnrolment(long id, Enrolment updateEnrolment) {
//        ResponseObjectEnrolment resp = new ResponseObjectEnrolment();
//        try {
//            Optional<Enrolment> existingEnrolmentOptional = enrolmentRepository.findById(id);
//
//            if (existingEnrolmentOptional.isPresent()){
//                Enrolment existingEnrolment = existingEnrolmentOptional.get();
//                System.out.println("existingEnrolment = " + updateEnrolment);
//                existingEnrolment.setCustomer(updateEnrolment.getCustomer());
//                existingEnrolment.setProduct(updateEnrolment.getProduct());
//                existingEnrolment.setStatus(updateEnrolment.getStatus());
//                existingEnrolment.setDate(updateEnrolment.getDate());
//                existingEnrolment.setAttachment(updateEnrolment.getAttachment());
//                existingEnrolment.setCreatedBy(updateEnrolment.getCreatedBy());
//                existingEnrolment.setUpdatedBy(updateEnrolment.getUpdatedBy());
//
//                Enrolment updateEnrolmentEntity = enrolmentRepository.save(existingEnrolment);
//
//                resp.setResponseCode("000");
//                resp.setResponseMessage("Success");
//                resp.setObject(updateEnrolmentEntity);
//            }
//            else {
//                resp.setResponseCode("E01");
//                resp.setResponseMessage("Enrolment not found for id: " + id);
//            }
//        }
//        catch (Exception e) {
//            resp.setResponseCode("E01");
//            resp.setResponseMessage("Failed to update enrolment: " + e.getMessage());
//        }
//        return resp;
//    }
@Override
public ResponseObjectEnrolment updateEnrolment(long id, EnrolmentRequestDTO updateEnrolmentDTO) {
    ResponseObjectEnrolment resp = new ResponseObjectEnrolment();

    try {
        Optional<Enrolment> existingEnrolmentOptional = enrolmentRepository.findById(id);

        if (existingEnrolmentOptional.isPresent()) {
            Enrolment existingEnrolment = existingEnrolmentOptional.get();

            // Update fields based on the EnrolmentRequestDTO
            existingEnrolment.setCustomer(customerRepository.findById(updateEnrolmentDTO.getCustomerId()).orElse(null));
            existingEnrolment.setProduct(productRepository.findById(updateEnrolmentDTO.getProductId()).orElse(null));
            existingEnrolment.setStatus(updateEnrolmentDTO.getStatus());
            existingEnrolment.setDate(updateEnrolmentDTO.getDate());
            existingEnrolment.setAttachment(updateEnrolmentDTO.getAttachment());
            existingEnrolment.setComments(updateEnrolmentDTO.getComments());
            existingEnrolment.setCreatedBy(updateEnrolmentDTO.getCreatedBy());
            existingEnrolment.setUpdatedBy(updateEnrolmentDTO.getUpdatedBy());

            Enrolment updatedEnrolmentEntity = enrolmentRepository.save(existingEnrolment);

            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(updatedEnrolmentEntity);
        } else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Enrolment not found for id: " + id);
        }
    } catch (Exception e) {
        resp.setResponseCode("E01");
        resp.setResponseMessage("Failed to update enrolment: " + e.getMessage());
    }

    return resp;
}


    @Override
    public ResponseObjectEnrolment deleteEnrolment(long id) {
        ResponseObjectEnrolment resp = new ResponseObjectEnrolment();
        Optional<Enrolment> existingEnrolment = enrolmentRepository.findById(id);

        if (existingEnrolment.isPresent()){
            enrolmentRepository.deleteById(existingEnrolment.get().getId());
            System.out.println("id deleted: " + existingEnrolment.get().getId());
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(existingEnrolment.get());
        }
        else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Product not found for id: " + id);
        }
        return resp;
    }

    @Override
    public ResponseObjectEnrolment uploadEnrolmentsFromExcel(MultipartFile file) {
        ResponseObjectEnrolment resp = new ResponseObjectEnrolment();

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
                Enrolment enrolment = new Enrolment();

                // Handle numeric cells
                DataFormatter dataFormatter = new DataFormatter();

                // Extract data into corresponding columns in the database
                enrolment.setCustomer(customerRepository.findById(Long.valueOf(dataFormatter.formatCellValue(row.getCell(0)))).orElse(null));
                 enrolment.setProduct(productRepository.findById(Long.valueOf(dataFormatter.formatCellValue(row.getCell(1)))).orElse(null));
                 enrolment.setStatus(dataFormatter.formatCellValue(row.getCell(2)));
                 String dateStr = dataFormatter.formatCellValue(row.getCell(3));
                 Date date = convertStringToDate(dateStr); // Implement this method to convert dateStr to Date
                 enrolment.setDate(date);
                 enrolment.setAttachment(dataFormatter.formatCellValue(row.getCell(4)));

                enrolmentRepository.save(enrolment);
            }

            resp.setResponseCode("000");
            resp.setResponseMessage("Data loaded successfully.");
        } catch (IOException e) {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to update enrolment: " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return resp;
    }

    private Date convertStringToDate(String dateStr) throws ParseException {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Parse the date string to a Date object
            return dateFormat.parse(dateStr);
        } catch (java.text.ParseException e) {
            // Handle the exception appropriately based on your requirements
            e.printStackTrace();
            return null;
        }
    }


    private boolean areHeadersMatching(Row headerRow) {
        // Compare the Excel file headers with the expected column names
        List<String> expectedHeaders = Arrays.asList(
                "customer_id", "product_id", "status",
                "due_date", "attachment"
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
    public byte[] exportEnrolmentsToExcel() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        // Example: Add headers to the sheet
        Row headerRow = sheet.createRow(0);
        List<String> headers = Arrays.asList(
                "customer_id", "product_id", "status",
                "due_date", "attachment"
        );
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
        }
        // Fetch customer data from the database
        List<Enrolment> enrolments = enrolmentRepository.findAll(); // Adjust this based on your repository method

        // Add data rows to the sheet
        for (int rowIndex = 0; rowIndex < enrolments.size(); rowIndex++) {
            Enrolment enrolment = enrolments.get(rowIndex);
            Row dataRow = sheet.createRow(rowIndex + 1); // Add 1 to skip the header row

            dataRow.createCell(0).setCellValue(enrolment.getCustomer().getId());
            dataRow.createCell(1).setCellValue(enrolment.getProduct().getId());
            dataRow.createCell(2).setCellValue(enrolment.getStatus());
            dataRow.createCell(3).setCellValue(enrolment.getDate());
//            dataRow.createCell(4).setCellValue(enrolment.getAttachment());
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

}
