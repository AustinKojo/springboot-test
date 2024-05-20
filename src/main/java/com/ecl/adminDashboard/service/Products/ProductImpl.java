package com.ecl.adminDashboard.service.Products;

import com.ecl.adminDashboard.dto.ResponseObjectProduct;
import com.ecl.adminDashboard.dto.ResponseObjectListProduct;
import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.model.Product;
import com.ecl.adminDashboard.repository.ProductRepository;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import  java.util.Optional;

import static org.hibernate.type.SqlTypes.JSON;

@Service
@AllArgsConstructor
public class ProductImpl implements ProductService {
    private ProductRepository productRepository;

    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB

    @Override
    public ResponseObjectProduct createProduct(Product product) {
        ResponseObjectProduct resp = new ResponseObjectProduct();

        // Check if the customer with the same companyName already exists
        Optional <Product> existingProduct = productRepository.findByProductNameAndState(product.getProductName(),
                "active");
        if (existingProduct.isPresent()) {
            // If the product with the same product name already exists, return an error response
            resp.setResponseCode("E02");
            resp.setResponseMessage("Product with the same product name already exists");
            return resp;
        }

        product.setState("active");
        Product newProduct = productRepository.save(product);
        System.out.println("this is product id " + newProduct.getId());

        if(newProduct.getId() != null) {
            System.out.println("new id " + newProduct.getId());
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(newProduct);
        }
        else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed");
        }
        return resp;
    }

    @Override
    public ResponseObjectListProduct getProduct(String productName) {
        ResponseObjectListProduct resp = new ResponseObjectListProduct();

        try {
            List<Product> products = productRepository.findByStateNotOrderByCreatedOnDesc("deleted");

            if (!products.isEmpty()){
                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject(products);
            }
            else {
                resp.setResponseCode("E01");
                resp.setResponseMessage("Error");
            }
        }
        catch (Exception e){
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to retrieve products: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public ResponseObjectProduct getProductById(long id) {
        ResponseObjectProduct resp =new ResponseObjectProduct();

        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            System.out.println("id found " + product.get().getId());
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(product.get());
        }
        else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed");
        }

        return resp;
    }

    @Override
    public ResponseObjectProduct updateProduct(long id, Product updateProduct) {
        ResponseObjectProduct resp = new ResponseObjectProduct();

        try {
            Optional<Product> existingProductOptional = productRepository.findById(id);

            if (existingProductOptional.isPresent()){
                Product existingProduct = existingProductOptional.get();
                System.out.println("existingProduct = " + updateProduct);
                existingProduct.setProductName(updateProduct.getProductName());
                existingProduct.setProductDetails(updateProduct.getProductDetails());
                existingProduct.setUpdatedBy(updateProduct.getUpdatedBy());

                Product updateProductEntity = productRepository.save(existingProduct);

                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject(updateProductEntity);
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
    public ResponseObjectProduct deleteProduct(long id) {
        ResponseObjectProduct resp = new ResponseObjectProduct();
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()){
            existingProduct.get().setState("deleted");
//            productRepository.deleteById(existingProduct.get().getId());
            productRepository.save(existingProduct.get());

            System.out.println("id deleted: " + existingProduct.get().getId());
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(existingProduct.get());
        }
        else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Product not found for id: " + id);
        }
        return resp;
    }

    @Override
    public ResponseObjectProduct uploadProductsFromExcel(MultipartFile file) {
        ResponseObjectProduct resp = new ResponseObjectProduct();

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
                Product product = new Product();

                // Handle numeric cells
                DataFormatter dataFormatter = new DataFormatter();

                // Extract data into corresponding columns in the database
                product.setProductName(dataFormatter.formatCellValue(row.getCell(0)));
                product.setProductDetails(dataFormatter.formatCellValue(row.getCell(1)));

                productRepository.save(product);
            }

            resp.setResponseCode("000");
            resp.setResponseMessage("Data loaded successfully.");
        } catch (IOException e) {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to update product: " + e.getMessage());
        }

        return resp;
    }

    private boolean areHeadersMatching(Row headerRow) {
        // Compare the Excel file headers with the expected column names
        List<String> expectedHeaders = Arrays.asList(
                "product_name", "product_details"
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
    public byte[] exportProductsToExcel() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        // Example: Add headers to the sheet
        Row headerRow = sheet.createRow(0);
        List<String> headers = Arrays.asList(
                "product_name", "product_details"
        );
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
        }
        // Fetch product data from the database
        List<Product> activeProducts = productRepository.findByState("active"); // Adjust this based on your repository method

        // Add data rows to the sheet
        for (int rowIndex = 0; rowIndex < activeProducts.size(); rowIndex++) {
            Product product = activeProducts.get(rowIndex);
            Row dataRow = sheet.createRow(rowIndex + 1); // Add 1 to skip the header row

            dataRow.createCell(0).setCellValue(product.getProductName());
            dataRow.createCell(1).setCellValue(product.getProductDetails());
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
