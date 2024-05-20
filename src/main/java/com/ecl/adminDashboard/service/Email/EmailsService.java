package com.ecl.adminDashboard.service.Email;

import com.ecl.adminDashboard.model.SLA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailImpl emailImpl;

    @PersistenceContext
    private EntityManager entityManager;

//    @Transactional
//    @Scheduled(cron = "0 * * * * *") // Execute every minute
//    public void sendRenewalReminderEmails() {
//        LOGGER.info("Scheduled task started at {}", new Date());
//        LocalDate currentDate = LocalDate.now();
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        LOGGER.info("Current date: {}", currentDate);
//
//        updateStatusToExpiredOnRenewalDate();
//
//        // Query to fetch SLA entries with renewalDate matching the current date
//        Query query = entityManager.createQuery(
//                "SELECT s FROM SLA s WHERE FUNCTION('DATE', s.renewalDate) = :currentDate",
//                SLA.class);
//        query.setParameter("currentDate", currentDate);
//
//        List<SLA> matchingSLAs = query.getResultList();
//        LOGGER.info("Found {} matching SLAs", matchingSLAs.size());
//
//        // Send email for each matching SLA
//        for (SLA sla : matchingSLAs) {
//            sendRenewalReminderEmail(sla);
//        }
//        LOGGER.info("Scheduled task finished at {}", new Date());
//    }
@Transactional
//@Scheduled(cron = "1 0 0 * * ?") //
public void sendRenewalReminderEmails() {
    LOGGER.info("Scheduled task started at {}", new Date());
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LOGGER.info("Current date: {}", currentDate);

    // Calculate one month from the current date
    LocalDate oneMonthLater = currentDate.plusMonths(1);
    LOGGER.info("One Month date: {}", oneMonthLater);

    // Query to fetch SLA entries with renewalDate one month from the current date
    Query query = entityManager.createQuery(
            "SELECT s FROM SLA s WHERE FUNCTION('DATE', s.renewalDate) = :oneMonthLater",
            SLA.class);
    query.setParameter("oneMonthLater", oneMonthLater);

    List<SLA> matchingSLAs = query.getResultList();
    LOGGER.info("Found {} matching SLAs", matchingSLAs.size());

    // Send email for each matching SLA
    for (SLA sla : matchingSLAs) {
        sendRenewalReminderEmail(sla);
    }
    LOGGER.info("Scheduled task finished at {}", new Date());
}

    public int calculateMatchingSLACount(LocalDate oneMonthLater){
        // Query to fetch SLA entries with renewalDate matching the current date
        Query query = entityManager.createQuery(
                "SELECT s FROM SLA s WHERE FUNCTION('DATE', s.renewalDate) = :oneMonthLater",
                SLA.class);
        query.setParameter("oneMonthLater", oneMonthLater);

        List<SLA> matchingSLAs = query.getResultList();
        int matchingSLACount = matchingSLAs.size();
        return matchingSLACount;
    }

    public List<String> getMatchingCompanyNames() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate oneMonthLater = currentDate.plusMonths(1);

        Query query = entityManager.createQuery(
                "SELECT DISTINCT s.companyName FROM SLA s WHERE FUNCTION('DATE', s.renewalDate) = :oneMonthLater",
                String.class);
        query.setParameter("oneMonthLater", oneMonthLater);

        List<String> matchingCompanyNames = query.getResultList();
        return matchingCompanyNames;
    }

    private void sendRenewalReminderEmail(SLA sla) {
        LOGGER.info("Sending renewal reminder email for SLA id {}", sla.getId());
        // Customize the email content as needed
        String to = "awotweaustin@gmail.com";
        String subject = "Renewal Reminder";
        String text = "Dea " + sla.getCompanyName() + ",\n\nThis a reminder to renew your agreement.";

        // Send the email
        emailImpl.sendEmail(to, subject, text);
    }

    @Transactional
    public void updateStatusToExpiredOnRenewalDate() {
        LocalDate currentDate = LocalDate.now();

        // Query to update the status to "expired" for SLAs with renewalDate equal to the current date
        Query query = entityManager.createQuery(
                "UPDATE SLA s SET s.status = 'EXPIRED' WHERE FUNCTION('DATE', s.renewalDate) = :currentDate");
        query.setParameter("currentDate", currentDate);

        query.executeUpdate();
    }
}
