package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.model.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseObjectProduct extends Response{
    private Product object;

}
