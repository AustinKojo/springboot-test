package com.ecl.adminDashboard.service.Email;

import com.ecl.adminDashboard.model.SLA;
import com.ecl.adminDashboard.model.Users;
import com.ecl.adminDashboard.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class SlaEmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlaEmailService.class);

    @Autowired
    private EnrolmentEmailImpl enrolmentEmailImpl;
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Scheduled(cron = "* 0 0 * * *") // Execute every day at midnight
    public void sendSlaRenewalReminderEmails() {
        LOGGER.info("Scheduled task started at {}", new Date());
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LOGGER.info("Current date: {}", currentDate);

        // List of CC emails
        List<String> ccEmails = Arrays.asList("e.ofori@ecl-global.com", "a.awotwe@ecl-global.com");

        // Calculate one month from the current date
        LocalDate date15Days = currentDate.plusDays(15);
        LocalDate date5Days = currentDate.plusDays(5);
        LocalDate date0Days = currentDate;
        LocalDate oneMonthLater = currentDate.plusMonths(1);
        LocalDate threeMonthLater = currentDate.plusMonths(3);

        LOGGER.info("One Month date: {}", oneMonthLater);

        // Query to fetch SLA entries with renewalDate one month from the current date
        Query query = entityManager.createQuery(
                "SELECT s FROM SLA s WHERE FUNCTION('DATE', s.renewalDate) IN (:date15Days, :date5Days, :date0Days, :oneMonthLater, :threeMonthLater)",
                SLA.class);
        query.setParameter("threeMonthLater", threeMonthLater);
        query.setParameter("oneMonthLater", oneMonthLater);
        query.setParameter("date15Days", date15Days);
        query.setParameter("date5Days", date5Days);
        query.setParameter("date0Days", date0Days);

        List<SLA> matchingSlas = query.getResultList();
        LOGGER.info("Found {} matching SLAs", matchingSlas.size());



        // Initialize StringBuilder for email content
        StringBuilder emailContent = new StringBuilder("Greetings Admin, \n\n");

        // Retrieve all users with the role "admin"
        List<Users> adminUsers = userRepository.findAll();

        // Send email for each matching SLA
        for (Users adminUser : adminUsers) {
            // Initialize StringBuilder for email content
            StringBuilder adminEmailContent = new StringBuilder();

            for (SLA sla : matchingSlas) {
                appendSlaDueDateReminder(sla, currentDate, adminEmailContent, adminUser.getEmail());
            }

            // Send a single email with all reminders
            if (adminEmailContent.length() > emailContent.length()) {
//            String to = "awotweaustin@gmail.com";
                String subject = "SLA Renewal Reminders";

                enrolmentEmailImpl.sendEmail(adminUser.getEmail(), subject, adminEmailContent.toString(), ccEmails);


                LOGGER.info("Scheduled task finished at {}", new Date());
            }
        }
        LOGGER.info("Scheduled task finished at {}", new Date());
    }

    private void appendSlaDueDateReminder(SLA sla, LocalDate currentDate, StringBuilder emailContent, String email) {
        LOGGER.info("Adding renewal reminder for SLA id {}", sla.getId());

        // Convert renewal date to LocalDate
        Instant instant = sla.getRenewalDate().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate renewalDate = instant.atZone(zoneId).toLocalDate();

        // Calculate the number of days left for renewal
        long daysLeft = ChronoUnit.DAYS.between(currentDate, renewalDate);

        // Customize the email content as needed
        emailContent.append(sla.getCompanyName()).append(" - ");

        if (daysLeft > 0) {
            emailContent.append(" Sla renewal due in ").append(daysLeft).append(" days.\n");
        } else {
            emailContent.append(" Sla renewal is due today. Please take action promptly!\n");
        }
    }

    public int calculateMatchingSlaCount(LocalDate threeMonthLater, LocalDate oneMonthLater, LocalDate date15Days, LocalDate date5Days, LocalDate date0Days){
        // Query to fetch SLA entries with renewalDate matching the current date
        Query query = entityManager.createQuery(
                "SELECT s FROM SLA s WHERE FUNCTION('DATE', s.renewalDate) IN (:threeMonthLater, :oneMonthLater, :date15Days, :date5Days, :date0Days)",
                SLA.class);
        query.setParameter("threeMonthLater", threeMonthLater);
        query.setParameter("oneMonthLater", oneMonthLater);
        query.setParameter("date15Days", date15Days);
        query.setParameter("date5Days", date5Days);
        query.setParameter("date0Days", date0Days);

        List<SLA> matchingSlas = query.getResultList();
        int matchingSlaCount = matchingSlas.size();
        return matchingSlaCount;
    }

    public List<String> getSlaMatchingCompanyNames(LocalDate threeMonthLater, LocalDate oneMonthLater, LocalDate date15Days, LocalDate date5Days, LocalDate date0Days) {
//        LocalDate currentDate = LocalDate.now();
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate oneMonthLater = currentDate.plusMonths(1);


        Query query = entityManager.createQuery(
                "SELECT DISTINCT companyName FROM SLA s WHERE FUNCTION('DATE', s.renewalDate) IN (:threeMonthLater, :oneMonthLater, :date15Days, :date5Days, :date0Days)",
                String.class);
        query.setParameter("threeMonthLater", threeMonthLater);
        query.setParameter("oneMonthLater", oneMonthLater);
        query.setParameter("date15Days", date15Days);
        query.setParameter("date5Days", date5Days);
        query.setParameter("date0Days", date0Days);

        List<String> matchingCompanyNames = query.getResultList();
        return matchingCompanyNames;
    }

    @Transactional
    public void updateStatusToExpiredOnRenewalDate() {
        LocalDate currentDate = LocalDate.now();

        // Query to update the status to "expired" for SLAs with renewalDate equal to the current date
        Query query = entityManager.createQuery(
                "UPDATE SLA s SET s.status = 'EXPIRED' WHERE FUNCTION('DATE', s.renewal_date) = :currentDate");
        query.setParameter("currentDate", currentDate);

        query.executeUpdate();
    }

}
