package com.ecl.adminDashboard.controller;

import com.ecl.adminDashboard.service.Email.EmailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailImpl emailService;

    @GetMapping("/send")
    public String sendEmail() {
        System.out.println("Email sent successfully");
        String to = "kojaustawotwe@gmail.com";
        String subject = "Test Email";
        String text = "Dear customer, your subscription expiry is due in 30 days. ";
        emailService.sendEmail(to, subject, text);
        return "Email sent successfully!";

    }

        @GetMapping("/test-send")
        public String testSendEmail() {
            emailService.sendEmail("kojaustawotwe@gmail.com", "Test Subject", "Test Body");
            return "Test email sent successfully!";
        }

}
