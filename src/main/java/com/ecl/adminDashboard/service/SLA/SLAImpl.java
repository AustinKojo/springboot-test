package com.ecl.adminDashboard.service.SLA;

import com.ecl.adminDashboard.dto.*;
import com.ecl.adminDashboard.model.SLA;
import com.ecl.adminDashboard.repository.SLARepository;

import com.ecl.adminDashboard.service.Email.SendNewSla;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SLAImpl implements SLAService{


    private SLARepository slaRepository;
    private static final long MAX_FILE_SIZE_BYTES = 100 * 1024 * 1024; // 100 MB

    @Autowired
    private SendNewSla sendNewSla;

    @Override
    public ResponseObjectSLA createSLA(SlaRequestDTO slaRequestDTO) {
        ResponseObjectSLA resp = new ResponseObjectSLA();
        SLA sla = new SLA();

        if(sla.getId() == null) {
            sla.setCompanyName(slaRequestDTO.getCompanyName());
            sla.setAgreementDetails(slaRequestDTO.getAgreementDetails());
//            sla.setStatus(slaRequestDTO.getStatus());
            sla.setRenewalDate(slaRequestDTO.getRenewalDate());
            sla.setAttachment(slaRequestDTO.getAttachment());
            sla.setPaymentDetails(slaRequestDTO.getPaymentDetails());
            sla.setCreatedOn(LocalDateTime.now());

            if (sla.getRenewalDate() != null) {
                Date currentDate = new Date();
                if (sla.getRenewalDate().before(currentDate)) {
                    sla.setStatus("EXPIRED");
                } else {
                    sla.setStatus("ACTIVE");
                }
            }

            SLA newSla = slaRepository.save(sla);

            sendNewSla.sendSlaCreatedEmail(newSla);

//            System.out.println("this is product id " + sla.getId());
            System.out.println("new id " + sla.getId());
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(newSla);
        }
        else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed");
        }
        return resp;
    }

    @Override
    public ResponseObjectListSLA getSLA(String companyName) {
        ResponseObjectListSLA resp = new ResponseObjectListSLA();

        try {
            List<SLA> sla = slaRepository.findAllByOrderByCreatedOnDesc();

            if (!sla.isEmpty()){
                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject(sla);
            }
            else {
                resp.setResponseCode("E01");
                resp.setResponseMessage("Error");
            }
        }
        catch (Exception e){
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to retrieve sla: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public ResponseObjectSLA updateSLA(long id, SLA updateSLA) {
        ResponseObjectSLA resp = new ResponseObjectSLA();

        try {
            Optional<SLA> existingSLAOptional = slaRepository.findById(id);

            if (existingSLAOptional.isPresent()){
                SLA existingSLA = existingSLAOptional.get();
                System.out.println("existingSLA = " + updateSLA);
                existingSLA.setCompanyName(updateSLA.getCompanyName());
                existingSLA.setAgreementDetails(updateSLA.getAgreementDetails());
                existingSLA.setStatus(updateSLA.getStatus());
                existingSLA.setRenewalDate(updateSLA.getRenewalDate());
                existingSLA.setAttachment(updateSLA.getAttachment());
                existingSLA.setPaymentDetails(updateSLA.getPaymentDetails());
                existingSLA.setUpdatedBy(updateSLA.getUpdatedBy());

                SLA updateSLAEntity = slaRepository.save(existingSLA);

                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject(updateSLAEntity);
            }
            else {
                resp.setResponseCode("000");
                resp.setResponseMessage("Product not found for id: " + id);
            }
        }
        catch (Exception e){
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to update customer: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public ResponseObjectSLA deleteSLA(long id) {
        ResponseObjectSLA resp = new ResponseObjectSLA();
        Optional<SLA> existingSLA = slaRepository.findById(id);

        if (existingSLA.isPresent()){
            slaRepository.deleteById(existingSLA.get().getId());
            System.out.println("id deleted: " + existingSLA.get().getId());
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(existingSLA.get());
        }
        else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Sla not found for id: " + id);
        }
        return resp;
    }

    @Override
    public ResponseObjectSLA uploadSlasFromExcel(MultipartFile file) {
        ResponseObjectSLA resp = new ResponseObjectSLA();

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
                SLA sla = new SLA();

                // Handle numeric cells
                DataFormatter dataFormatter = new DataFormatter();

                // Extract data into corresponding columns in the database
                sla.setCompanyName(dataFormatter.formatCellValue(row.getCell(0)));
                sla.setAgreementDetails(dataFormatter.formatCellValue(row.getCell(1)));
                sla.setStatus(dataFormatter.formatCellValue(row.getCell(2)));
                // Convert the string to Date using an appropriate format
                String dateStrStart = dataFormatter.formatCellValue(row.getCell(3));
                LocalDateTime dateStart = convertStringToDateStart(dateStrStart);
                sla.setCreatedOn(dateStart);

                String dateStr = dataFormatter.formatCellValue(row.getCell(4));
                Date date = convertStringToDate(dateStr);
                sla.setRenewalDate(date);

                slaRepository.save(sla);
            }

            // Save the new SLAs at the beginning of the database table

            resp.setResponseCode("000");
            resp.setResponseMessage("Data loaded successfully.");
        } catch (IOException | ParseException | java.text.ParseException e) {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to update sla: " + e.getMessage());
        }

        return resp;
    }

    // Implement a method to convert String to Date
    private Date convertStringToDate(String dateStr) throws ParseException, java.text.ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.parse(dateStr);
    }

    private LocalDateTime convertStringToDateStart(String dateStrStart) {
        LocalDate date = LocalDate.parse(dateStrStart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // Set a default time, for example, midnight (00:00)
        return date.atStartOfDay();
    }


    private boolean areHeadersMatching(Row headerRow) {
        // Compare the Excel file headers with the expected column names
        List<String> expectedHeaders = Arrays.asList(
                "company_name", "agreement_details", "status", "created_on", "renewal_date"
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
    public byte[] exportSlasToExcel() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sla");

        // Example: Add headers to the sheet
        Row headerRow = sheet.createRow(0);
        List<String> headers = Arrays.asList(
                "company_name", "agreement_details", "status", "created_on", "renewal_date"
        );
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
        }
        // Fetch product data from the database
        List<SLA> slas = slaRepository.findAll(); // Adjust this based on your repository method

        // Add data rows to the sheet
        for (int rowIndex = 0; rowIndex < slas.size(); rowIndex++) {
            SLA sla = slas.get(rowIndex);
            Row dataRow = sheet.createRow(rowIndex + 1); // Add 1 to skip the header row

            dataRow.createCell(0).setCellValue(sla.getCompanyName());
            dataRow.createCell(1).setCellValue(sla.getAgreementDetails());
            dataRow.createCell(2).setCellValue(sla.getStatus());
            dataRow.createCell(3).setCellValue(sla.getCreatedOn());
            dataRow.createCell(4).setCellValue(sla.getRenewalDate());
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
