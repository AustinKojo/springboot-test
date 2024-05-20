package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.model.Users;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ResponseObjectListUsers extends Response{
    private List<Users> object;
}
