package com.ecl.adminDashboard.dto;
import com.ecl.adminDashboard.model.Product;
import lombok.Setter;
import lombok.Getter;

import java.util.List;

@Getter
@Setter
public class ResponseObjectListProduct extends Response{
    private List<Product> object;
}
