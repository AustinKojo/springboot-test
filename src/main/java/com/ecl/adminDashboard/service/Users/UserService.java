package com.ecl.adminDashboard.service.Users;
import com.ecl.adminDashboard.dto.ResponseObjectListUsers;
import com.ecl.adminDashboard.dto.ResponseObjectProduct;
import com.ecl.adminDashboard.dto.ResponseObjectUsers;
import com.ecl.adminDashboard.model.ChangePasswordRequest;
import com.ecl.adminDashboard.model.Product;
import com.ecl.adminDashboard.model.Users;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    ResponseObjectUsers createUser(Users user);

    ResponseObjectListUsers getAllUsers();

    ResponseObjectUsers getUserByUsername(String username);


    ResponseObjectUsers getUserByRole(String role);

    ResponseObjectUsers authenticateUser(String username, String password);

    ResponseObjectUsers changeUserPassword(Long id, ChangePasswordRequest changePasswordRequest);

    ResponseObjectUsers deleteUser(Long userId);

    public ResponseObjectUsers updateUser (long id, Users updateUser);


    // Add other methods as needed

//    @Autowired
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
//
////    public Optional<Users> authenticateUser(String username, String password) {
////        return userRepository.findByUsernameAndPassword(username, password);
////    }
//
//    public Optional<Users> getUserByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//
//    public List<Users> getAllUsers() {
//        return userRepository.findAll();
//    }
//
////    public Users createUser(Users user) {
////        return userRepository.save(user);
////    }
//
//    public void deleteUser(Long userId) {
//        userRepository.deleteById(userId);
//    }
//
//    @Autowired
//    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public Users createUser(Users user) {
//        // Hash the password before saving it
//        String hashedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(hashedPassword);
//        return userRepository.save(user);
//    }
//
//    public Optional<Users> authenticateUser(String username, String password) {
//        Optional<Users> user = userRepository.findByUsername(username);
//        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
//            return user;
//        }
//        return Optional.empty();
//    }
//
//    public Optional<Users> editUserPassword(String username, String newPassword) {
//        return Optional.empty();
//    }


}
