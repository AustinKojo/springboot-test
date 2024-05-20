package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.model.Enrolment;
import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.model.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ResponseObjectEnrolment extends Response{
    private Enrolment object;

}
