package com.ecl.adminDashboard.service.Email;

import com.ecl.adminDashboard.model.Enrolment;
import com.ecl.adminDashboard.model.SLA;
import com.ecl.adminDashboard.model.Users;
import com.ecl.adminDashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class SendNewSla {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrolmentEmailImpl enrolmentEmailImpl;

    List<String> ccEmails = Arrays.asList("cc1@gmail.com", "cc2@gmail.com");

    public void sendSlaCreatedEmail(SLA sla) {
//        LOGGER.info("Sending email for newly created enrolment with ID: {}", enrolment.getId());
        List<Users> adminUsers = userRepository.findByRole("Finance");

        // Customize the email content as needed
//        String to = "recipient@example.com"; // Set recipient email address
        String subject = "New SLA Created";
        String text = "A new SLA has been created for "
                + sla.getCompanyName() + " with the following details: " + "\n" + "\n"
                + "Agreement Details: " + sla.getAgreementDetails() + "\n"
                + "Payment Details: " + sla.getPaymentDetails() + "\n"
                + "Start date: " + sla.getCreatedOn() + "\n"
                + "Renewal date: " + sla.getRenewalDate() + "\n";

        for (Users adminUser : adminUsers) {
            enrolmentEmailImpl.sendEmail(adminUser.getEmail(), subject, text, ccEmails);
        }
    }

}
