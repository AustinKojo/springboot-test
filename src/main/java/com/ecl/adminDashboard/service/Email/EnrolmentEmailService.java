package com.ecl.adminDashboard.service.Email;

import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.model.Enrolment;
import com.ecl.adminDashboard.model.Users;
import com.ecl.adminDashboard.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EnrolmentEmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnrolmentEmailService.class);

    @Autowired
    private EnrolmentEmailImpl enrolmentEmailImpl;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Scheduled(cron = "* 0 0 * * *")
    public void sendEnrolRenewalReminderEmails() {
        LOGGER.info("Scheduled task started at {}", new Date());
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LOGGER.info("Current date: {}", currentDate);

        // List of CC emails
        List<String> ccEmails = Arrays.asList("e.ofori@ecl-global.com","a.awotwe@ecl-global.com");

        // Calculate one month from the current date
        LocalDate date15Days = currentDate.plusDays(15);
        LocalDate date5Days = currentDate.plusDays(5);
        LocalDate date0Days = currentDate;
        LocalDate oneMonthLater = currentDate.plusMonths(1);
        LOGGER.info("One Month date: {}", oneMonthLater);

        // Query to fetch Enrolment entries with renewalDate one month from the current date
        Query query = entityManager.createQuery(
                "SELECT s FROM Enrolment s WHERE FUNCTION('DATE', s.date) IN (:date15Days, :date5Days, :date0Days, :oneMonthLater)",
                Enrolment.class);
        query.setParameter("oneMonthLater", oneMonthLater);
        query.setParameter("date15Days", date15Days);
        query.setParameter("date5Days", date5Days);
        query.setParameter("date0Days", date0Days);

        List<Enrolment> matchingEnrolments = query.getResultList();
        LOGGER.info("Found {} matching Enrolments", matchingEnrolments.size());

        // Initialize StringBuilder for email content
        StringBuilder emailContent = new StringBuilder("Greetings Admin, \n\n");


        // Add introductory message only once
        emailContent.append("Greetings Admin, \n\n");

        // Retrieve all users with the role "admin"
        List<Users> adminUsers = userRepository.findAll();

        // Send email for each matching SLA
        for (Users adminUser: adminUsers) {
            StringBuilder adminEmailContent = new StringBuilder();

            for (Enrolment enrolment : matchingEnrolments) {
                appendEnrolmentDueDateReminder(enrolment, currentDate, adminEmailContent, adminUser.getEmail());
            }

        // Send a single email with all reminders
        if (adminEmailContent.length() > emailContent.length()) {
//            String to = "awotweaustin@gmail.com";
            String subject = "Enrolment Renewal Reminders";

                enrolmentEmailImpl.sendEmail(adminUser.getEmail(), subject, adminEmailContent.toString(), ccEmails);

                LOGGER.info("Scheduled task finished at {}", new Date());
        }
        }
        LOGGER.info("Scheduled task finished at {}", new Date());
    }

    private void appendEnrolmentDueDateReminder(Enrolment enrolment, LocalDate currentDate, StringBuilder emailContent, String email) {
        LOGGER.info("Adding renewal reminder for Enrolment id {}", enrolment.getId());

        // Check if customer is associated with the enrolment
        Customer customer = enrolment.getCustomer();

        if (customer != null) {
            // Convert renewal date to LocalDate
            Instant instant = enrolment.getDate().toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDate renewalDate = instant.atZone(zoneId).toLocalDate();

            // Calculate the number of days left for renewal
            long daysLeft = ChronoUnit.DAYS.between(currentDate, renewalDate);

            // Customize the email content as needed
            emailContent.append(customer.getCompanyName()).append(" - ");

            if (daysLeft > 0) {
                emailContent.append(" Enrolment renewal due in ").append(daysLeft).append(" days.\n");
            } else {
                emailContent.append(" Enrolment renewal is due today. Please take action promptly!\n");
            }
        }
    }

    public int calculateMatchingEnrolmentCount(LocalDate oneMonthLater, LocalDate date15Days, LocalDate date5Days, LocalDate date0Days){
        // Query to fetch SLA entries with renewalDate matching the current date
        Query query = entityManager.createQuery(
                "SELECT s FROM Enrolment s WHERE FUNCTION('DATE', s.date) IN (:oneMonthLater, :date15Days, :date5Days, :date0Days)",
                Enrolment.class);
        query.setParameter("oneMonthLater", oneMonthLater);
        query.setParameter("date15Days", date15Days);
        query.setParameter("date5Days", date5Days);
        query.setParameter("date0Days", date0Days);

        List<Enrolment> matchingEnrolments = query.getResultList();
        int matchingEnrolmentCount = matchingEnrolments.size();
        return matchingEnrolmentCount;
    }

    public List<String> getEnrolmentMatchingCompanyNames(LocalDate oneMonthLater, LocalDate date15Days, LocalDate date5Days, LocalDate date0Days) {
//        LocalDate currentDate = LocalDate.now();
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate oneMonthLater = currentDate.plusMonths(1);


        Query query = entityManager.createQuery(
                "SELECT DISTINCT s.customer.companyName FROM Enrolment s WHERE FUNCTION('DATE', s.date) IN (:oneMonthLater, :date15Days, :date5Days, :date0Days)",
                String.class);
        query.setParameter("oneMonthLater", oneMonthLater);
        query.setParameter("date15Days", date15Days);
        query.setParameter("date5Days", date5Days);
        query.setParameter("date0Days", date0Days);

        List<String> matchingCompanyNames = query.getResultList();
        return matchingCompanyNames;
    }

//    private void sendEnrolDueDateReminderEmail(Enrolment enrolment, LocalDate currentDate) {
//        LOGGER.info("Sending renewal reminder email for Enrolment id {}", enrolment.getId());
//        Customer customer = enrolment.getCustomer();
//
//        if (customer != null) {
//            // Convert enrolment date to LocalDate
//            Instant instant = enrolment.getDate().toInstant();
//            ZoneId zoneId = ZoneId.systemDefault();
//            LocalDate enrolmentDate = instant.atZone(zoneId).toLocalDate();
//            // Calculate the number of days left for renewal
//            long daysLeft = ChronoUnit.DAYS.between(currentDate, enrolmentDate);
//            // Customize the email content as needed
//
//            String to = "awotweaustin@gmail.com";
//            String subject = "Renewal Reminder";
//            String text = "Dear User, \n\n";
//
//            if (daysLeft > 0) {
////                text += "This is a reminder to renew your agreement. You have " + daysLeft + " days left.";
//                text += "This is a reminder that " + customer.getCompanyName() + " has an SLA expiry due in " + daysLeft + " days." ;
//
//            } else {
////                text += "Your agreement is due for renewal today. Please take action promptly.";
//                text += "This is a reminder that " + customer.getCompanyName() + " has an SLA expiry due today. Please take action promptly.";
//
//            }
//
//            // Send the email
//            enrolmentEmailImpl.sendEmail(to, subject, text);
//        } else {
//            LOGGER.warn("No associated customer found for Enrolment id {}", enrolment.getId());
//        }
//    }

    @Transactional
    public void updateStatusToExpiredOnRenewalDate() {
        LocalDate currentDate = LocalDate.now();

        // Query to update the status to "expired" for SLAs with renewalDate equal to the current date
        Query query = entityManager.createQuery(
                "UPDATE Enrolment s SET s.status = 'EXPIRED' WHERE FUNCTION('DATE', s.date) = :currentDate");
        query.setParameter("currentDate", currentDate);

        query.executeUpdate();
    }


}



