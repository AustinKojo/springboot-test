package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.model.Users;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class ResponseObjectUsers extends Response{

    private List<UserDetails> object;

    @Setter
    @Getter
    public static class UserDetails {
        private Long id;
        private String username;
        private String name;
        private String email;
        private String contactNumber;
        private String role;
        private Long defaultPassword;
    }

    public static ResponseObjectUsers fromUser(Users user) {
        ResponseObjectUsers responseObject = new ResponseObjectUsers();
        UserDetails userDetails = new UserDetails();

        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setName(user.getName());
        userDetails.setEmail(user.getEmail());
        userDetails.setContactNumber(user.getContactNumber());
        userDetails.setRole(user.getRole());
        userDetails.setDefaultPassword(user.getDefaultPassword());
        // Set other fields as needed
        responseObject.setObject(List.of(userDetails));
        responseObject.setResponseCode("000");
        responseObject.setResponseMessage("User successfully created");
        return responseObject;
    }}
