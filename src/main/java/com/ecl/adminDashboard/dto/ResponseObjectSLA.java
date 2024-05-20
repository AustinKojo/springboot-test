package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.model.SLA;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseObjectSLA extends Response{
    private SLA object;

}
