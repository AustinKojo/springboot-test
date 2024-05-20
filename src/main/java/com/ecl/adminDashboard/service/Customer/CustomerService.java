package com.ecl.adminDashboard.service.Customer;

import com.ecl.adminDashboard.dto.Response;
import com.ecl.adminDashboard.dto.ResponseObject;
import com.ecl.adminDashboard.dto.ResponseObjectList;
import com.ecl.adminDashboard.model.Customer;
import org.springframework.web.multipart.MultipartFile;


public interface CustomerService {

    public ResponseObject createCustomer (Customer customer);

    public ResponseObjectList getCustomer(String companyName);

    public ResponseObject getCustomerById(long id);

   public  ResponseObject updateCustomer(long id, Customer updateCustomer);

    public ResponseObject deleteCustomer(long id);

    public ResponseObject uploadCustomersFromExcel(MultipartFile file);

    byte[] exportCustomersToExcel();

}
