package com.ecl.adminDashboard.service.Products;

import com.ecl.adminDashboard.dto.ResponseObject;
import com.ecl.adminDashboard.dto.ResponseObjectListProduct;
import com.ecl.adminDashboard.dto.ResponseObjectProduct;
import com.ecl.adminDashboard.model.Product;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    public ResponseObjectProduct createProduct(Product product);

    public ResponseObjectListProduct getProduct(String productName);

    public ResponseObjectProduct getProductById (long id);

    public ResponseObjectProduct updateProduct (long id, Product updateProduct);

    public ResponseObjectProduct deleteProduct(long id);

    public ResponseObjectProduct uploadProductsFromExcel(MultipartFile file);

    byte[] exportProductsToExcel();
}
