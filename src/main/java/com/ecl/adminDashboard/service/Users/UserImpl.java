package com.ecl.adminDashboard.service.Users;

import com.ecl.adminDashboard.dto.ResponseObjectListUsers;
import com.ecl.adminDashboard.dto.ResponseObjectProduct;
import com.ecl.adminDashboard.dto.ResponseObjectUsers;
import com.ecl.adminDashboard.model.ChangePasswordRequest;
import com.ecl.adminDashboard.model.Product;
import com.ecl.adminDashboard.model.Users;
import com.ecl.adminDashboard.repository.UserRepository;
import com.ecl.adminDashboard.service.Users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class  UserImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    @Override
    public ResponseObjectUsers createUser(Users user) {
        String generatedPassword = generateRandomPassword(7);
        String encodedPassword = passwordEncoder.encode(generatedPassword);
        user.setPassword(encodedPassword);
        user.setDefaultPassword(1L);

        ResponseObjectUsers resp = new ResponseObjectUsers();

        // Check if the user with the same username already exists
        Users existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            // If the user with the same username already exists, return an error response
            resp.setResponseCode("E02");
            resp.setResponseMessage("User with the same username already exists");
            return resp;
        }
        Users newUser = userRepository.save(user);

        if (newUser.getId() != null) {
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            sendUserCredentialsEmail(user.getUsername(), generatedPassword, user.getEmail());

            resp = ResponseObjectUsers.fromUser(user);
        } else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to create user");
        }
        return resp;
    }

    private String generateRandomPassword(int length) {
        // Implement your logic to generate a random string of the specified length
        // For simplicity, let's assume you have a utility method for this
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        // Implement your logic to generate a random string
        // This is a simplified example, and you might want to use a more robust approach
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }

    private void sendUserCredentialsEmail(String username, String password, String userEmail) {
        // Create a SimpleMailMessage and set the necessary details
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEmail);
        mailMessage.setSubject("Your Account Credentials");
        mailMessage.setText("Dear " + username + ",\n\nYour ECL Dashboard account has been created.\nUsername: " + username + "\nPassword: " + password);

        // Send the email
        javaMailSender.send(mailMessage);
    }

    @Override
    public ResponseObjectListUsers getAllUsers() {
        ResponseObjectListUsers resp = new ResponseObjectListUsers();
        try {
            List<Users> users = userRepository.findAllByOrderByCreatedOnDesc();

            if (!users.isEmpty()) {
                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject(users);
            } else {
                resp.setResponseCode("E01");
                resp.setResponseMessage("No users found");
            }
        }
        catch (Exception e){
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to retrieve users: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public ResponseObjectUsers getUserByUsername(String username) {
        ResponseObjectUsers resp = new ResponseObjectUsers();
        Users user = userRepository.findByUsername(username);

        if (user != null) {
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp = ResponseObjectUsers.fromUser(user);
        } else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("User not found");
        }
        return resp;
    }

    @Override
    public ResponseObjectUsers getUserByRole(String role) {
        ResponseObjectUsers resp = new ResponseObjectUsers();
        List<Users> user = userRepository.findByRole(role);

        if(user != null){
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp = ResponseObjectUsers.fromUser((Users) user);
        } else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("User not found");
        }
        return resp;
    }

    @Override
    public ResponseObjectUsers authenticateUser(String username, String password) {
        ResponseObjectUsers resp = new ResponseObjectUsers();
        Users user = userRepository.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            resp.setResponseCode("000");
            resp.setResponseMessage("Authentication successful");
            resp = ResponseObjectUsers.fromUser(user);
        } else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Authentication failed");
        }
        return resp;
    }

    @Override
    public ResponseObjectUsers changeUserPassword(Long id, ChangePasswordRequest changePasswordRequest) {
        ResponseObjectUsers resp = new ResponseObjectUsers();
        Users user = userRepository.findById(id).orElse(null);

        if (user != null && passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
                // Update the password
                String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
                user.setPassword(encodedPassword);
                user.setDefaultPassword(0L);
//                user.setLastPasswordChange(LocalDateTime.now());

                Users updatedUser = userRepository.save(user);

                resp.setResponseCode("000");
                resp.setResponseMessage("Password changed successfully");
                resp = ResponseObjectUsers.fromUser(user);
            } else {
                resp.setResponseCode("E02");
                resp.setResponseMessage("New password and confirm new password do not match");
            }
        } else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("User not found");
        }
        return resp;
    }

    @Override
    public ResponseObjectUsers deleteUser(Long userId) {
        ResponseObjectUsers resp = new ResponseObjectUsers();
        userRepository.deleteById(userId);
        resp.setResponseCode("000");
        resp.setResponseMessage("User deleted successfully");
        return resp;
    }

    @Override
    public ResponseObjectUsers updateUser(long id, Users updateUser) {
        ResponseObjectUsers resp = new ResponseObjectUsers();

        try {
            Optional<Users> existingUserOptional = userRepository.findById(id);

            if (existingUserOptional.isPresent()){
                Users existingUser = existingUserOptional.get();
                System.out.println("existingUser = " + updateUser);
                existingUser.setUsername(updateUser.getUsername());
                existingUser.setName(updateUser.getName());
                existingUser.setEmail(updateUser.getEmail());
                existingUser.setContactNumber(updateUser.getContactNumber());
                existingUser.setRole(updateUser.getRole());

//                existingUser.setPassword(updateUser.getProductDetails());
                existingUser.setUpdatedBy(updateUser.getUpdatedBy());

                Users updateUserEntity = userRepository.save(existingUser);

                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject((List<ResponseObjectUsers.UserDetails>) updateUserEntity);
            }
            else {
                resp.setResponseCode("000");
                resp.setResponseMessage("Product not found for id: " + id);
            }
        }
        catch (Exception e){
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to update customer: " + e.getMessage());
        }
        return resp;
    }


//    private final BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    public UserImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
//        super(userRepository, passwordEncoder);
//        this.passwordEncoder = passwordEncoder;
//    }
//
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public List<Users> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    @Override
//    public Users createUser(Users user) {
//        // Hash the password before saving it
//        String hashedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(hashedPassword);
//        return super.createUser(user);
//    }
//
//    @Override
//    public void deleteUser(Long userId) {
//        userRepository.deleteById(userId);
//    }
//
//    @Override
//    public Optional<Users> authenticateUser(String username, String password) {
//        Optional<Users> user = super.getUserByUsername(username);
//        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
//            return user;
//        }
//        return Optional.empty();
//    }
//
//    @Override
//    public Optional<Users> editUserPassword(String username, String newPassword) {
//        Optional<Users> optionalUser = userRepository.findByUsername(username);
//        if (optionalUser.isPresent()) {
//            Users user = optionalUser.get();
//            String hashedPassword = passwordEncoder.encode(newPassword);
//            user.setPassword(hashedPassword);
//
//            userRepository.save(user);
//            return Optional.of(user);
//        }
//        return Optional.empty();
//    }
}
