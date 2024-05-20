package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.model.Enrolment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ResponseObjectListEnrolment extends Response{
    private List<Enrolment> object;

}
