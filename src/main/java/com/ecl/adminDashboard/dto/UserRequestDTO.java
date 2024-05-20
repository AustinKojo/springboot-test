package com.ecl.adminDashboard.dto;

import com.ecl.adminDashboard.model.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String contactNumber;
    private String role;
    private String createdBy;
    private String updatedBy;

    public static UserRequestDTO fromEntity(Users user) {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        // Map other fields
        return dto;
    }

}
