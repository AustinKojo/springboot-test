package com.ecl.adminDashboard.dto;
import com.ecl.adminDashboard.model.Customer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter

public class ResponseObjectList extends Response{
    private List<Customer> object;
}
