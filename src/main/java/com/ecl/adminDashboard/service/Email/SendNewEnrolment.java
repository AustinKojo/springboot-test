package com.ecl.adminDashboard.service.Email;

import com.ecl.adminDashboard.model.Enrolment;
import com.ecl.adminDashboard.model.Users;
import com.ecl.adminDashboard.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendNewEnrolment {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrolmentEmailImpl enrolmentEmailImpl;

    List<String> ccEmails = Arrays.asList("cc1@gmail.com", "cc2@gmail.com");

    public void sendEnrolmentCreatedEmail(Enrolment enrolment) {
//        LOGGER.info("Sending email for newly created enrolment with ID: {}", enrolment.getId());
        List<Users> adminUsers = userRepository.findByRole("User");

        // Customize the email content as needed
//        String to = "recipient@example.com"; // Set recipient email address
        String subject = "New Enrolment Created";
        String text = "A new enrolment has been created for "
                + enrolment.getCustomer().getCompanyName() + ". "
                + enrolment.getComments() + "\n"

        // Add more details as needed
        ;

        for (Users adminUser : adminUsers) {
            enrolmentEmailImpl.sendEmail(adminUser.getEmail(), subject, text, ccEmails);
        }
    }

}
