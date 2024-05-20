package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.model.Documents;
import com.ecl.adminDashboard.model.Enrolment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseObjectListDocument extends Response{
    private List<Documents> object;

}
