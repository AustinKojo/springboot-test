package com.ecl.adminDashboard.controller;

import com.ecl.adminDashboard.dto.ResponseObjectListProduct;
import com.ecl.adminDashboard.dto.ResponseObjectProduct;
import com.ecl.adminDashboard.model.Product;
import com.ecl.adminDashboard.service.Products.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("eclDashboard/api/v1")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @PostMapping(value = "/products")
    public ResponseObjectProduct createProduct (@RequestBody Product product){
        return productService.createProduct(product);
    }

    @GetMapping(value = "/products")
    public ResponseObjectListProduct getProduct (@RequestParam(required = false) String productName){
        return productService.getProduct(productName);
    }

    @GetMapping(value = "/products/{id}")
    public ResponseObjectProduct getProductById (@PathVariable("id") long id){
        return productService.getProductById(id);
    }

    @PutMapping(value = "/products/{id}")
    public ResponseObjectProduct updateProduct(@PathVariable("id") long id, @RequestBody Product product){
        return productService.updateProduct(id, product);
    }

    @DeleteMapping(value = "/products/{id}")
    public ResponseObjectProduct deleteProduct(@PathVariable("id") long id){
        return productService.deleteProduct(id);
    }

    @PostMapping("/products/upload")
    public ResponseEntity<ResponseObjectProduct> uploadProductsFromExcel(@RequestParam("file") MultipartFile file) {
        ResponseObjectProduct response = productService.uploadProductsFromExcel(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/download/excel")
    public ResponseEntity<byte[]> downloadProductsAsExcel() {
        byte[] excelBytes = productService.exportProductsToExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "products.xlsx");
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
}
