//package com.ecl.adminDashboard.service.Users;
//
//import com.ecl.adminDashboard.dto.ResponseObject;
//import com.ecl.adminDashboard.dto.ResponseObjectListUsers;
//import com.ecl.adminDashboard.dto.ResponseObjectUsers;
//import com.ecl.adminDashboard.model.Users;
//
//public interface UsersService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public Optional<User> authenticateUser(String username, String password) {
//        return userRepository.findByUsernameAndPassword(username, password);
//    }
////    public ResponseObjectUsers createUser (Users users);
//
////    public ResponseObjectListUsers getUsers (String name);
////
////    public ResponseObjectUsers getUsersById(long id);
//////
////    public ResponseObjectUsers getUserByCredentials(String username, String password);
////
////    public ResponseObjectUsers authenticateUser(String username, String password);
////
//
//
//}
