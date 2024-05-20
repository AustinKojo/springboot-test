package com.ecl.adminDashboard.service.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrolmentEmailImpl {
    @Autowired
    private JavaMailSender javaMailSender;


    public void sendEmail(String to,
                          String subject,
                          String text,
                          List<String> ccEmails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);


        // If CC addresses are provided, add them to the message
        if (ccEmails != null && !ccEmails.isEmpty()) {
            message.setCc(ccEmails.toArray(new String[0])); // Convert List to array
        }
        javaMailSender.send(message);
    }

}
