package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.model.SLA;
import lombok.Setter;
import lombok.Getter;
import java.util.List;

@Setter
@Getter
public class ResponseObjectListSLA extends Response{
    private List<SLA> object;

}
