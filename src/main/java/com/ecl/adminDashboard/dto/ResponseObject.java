package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.model.Customer;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ResponseObject extends Response{

    private Customer object;



}

