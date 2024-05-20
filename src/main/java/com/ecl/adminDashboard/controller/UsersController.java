package com.ecl.adminDashboard.controller;


import com.ecl.adminDashboard.dto.*;
import com.ecl.adminDashboard.model.ChangePasswordRequest;
import com.ecl.adminDashboard.model.Product;
import com.ecl.adminDashboard.model.Users;
import com.ecl.adminDashboard.service.Users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("eclDashboard/api/v1")
@AllArgsConstructor
public class UsersController {
    private UserService userService;

    @PostMapping("/users")
    public ResponseObjectUsers createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }

    @GetMapping("/users")
    public ResponseObjectListUsers getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{username}")
    public ResponseObjectUsers getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        ResponseObjectUsers authenticationResponse = userService.authenticateUser(username, password);

        if ("000".equals(authenticationResponse.getResponseCode())) {
            // Authentication successful
            return ResponseEntity.ok(authenticationResponse);
        } else {
            // Authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponse);
        }
    }
@PutMapping("/users/{id}/change-password")
public ResponseObjectUsers changeUserPassword(@PathVariable Long id, @RequestBody ChangePasswordRequest changePasswordRequest) {
    return userService.changeUserPassword(id, changePasswordRequest);
}



@DeleteMapping("/users/{userId}")
    public ResponseObjectUsers deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);

    }

    @PutMapping(value = "/users/{id}")
    public ResponseObjectUsers updateUser(@PathVariable("id") long id, @RequestBody Users user){
        return userService.updateUser(id, user);
    }

}
