package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.model.Documents;
import com.ecl.adminDashboard.model.Enrolment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseObjectDocument extends Response{
    private Documents object;

}
