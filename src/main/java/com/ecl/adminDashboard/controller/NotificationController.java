package com.ecl.adminDashboard.controller;

import com.ecl.adminDashboard.service.Email.EmailsService;
import com.ecl.adminDashboard.service.Email.EnrolmentEmailService;
import com.ecl.adminDashboard.service.Email.SlaEmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("eclDashboard/api/v1/notifications")
@AllArgsConstructor
public class NotificationController {

    @Autowired
    private EmailsService emailsService;

    @Autowired
    private EnrolmentEmailService enrolmentEmailService;

    @Autowired
    private SlaEmailService slaEmailService;

    @GetMapping("/count")
    public ResponseEntity<Integer> getNotificationCount() {
        LocalDate currentDate = LocalDate.now();
        LocalDate oneMonthLater = currentDate.plusMonths(1);
        int notificationCount = emailsService.calculateMatchingSLACount(oneMonthLater);
        return ResponseEntity.ok(notificationCount);
    }

    @GetMapping("/expired/companyName")
    public ResponseEntity<List<String>> getMatchingCompanyNames() {
        List<String> companyNames = emailsService.getMatchingCompanyNames();
        return ResponseEntity.ok(companyNames);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/enrolment/count")
    public ResponseEntity<Integer> getEnrolmentNotificationCount() {
        LocalDate currentDate = LocalDate.now();
        LocalDate oneMonthLater = currentDate.plusMonths(1);
        LocalDate date15Days = currentDate.plusDays(15);
        LocalDate date5Days = currentDate.plusDays(5);
        LocalDate date0Days = currentDate;
        int notificationCount = enrolmentEmailService.calculateMatchingEnrolmentCount(oneMonthLater,date15Days, date5Days, date0Days);
        return ResponseEntity.ok(notificationCount);
    }

    @GetMapping("/enrolment/companyName")
    public ResponseEntity<List<String>> getEnrolmentMatchingCompanyNames() {
        LocalDate currentDate = LocalDate.now();
        LocalDate oneMonthLater = currentDate.plusMonths(1);
        LocalDate date15Days = currentDate.plusDays(15);
        LocalDate date5Days = currentDate.plusDays(5);
        LocalDate date0Days = currentDate;
        List<String> companyNames = enrolmentEmailService.getEnrolmentMatchingCompanyNames(oneMonthLater,date15Days, date5Days, date0Days);
        return ResponseEntity.ok(companyNames);
    }

    @GetMapping("/sla/count")
    public ResponseEntity<Integer> getSlaNotificationCount() {
        LocalDate currentDate = LocalDate.now();
        LocalDate threeMonthLater = currentDate.plusMonths(3);
        LocalDate oneMonthLater = currentDate.plusMonths(1);
        LocalDate date15Days = currentDate.plusDays(15);
        LocalDate date5Days = currentDate.plusDays(5);
        LocalDate date0Days = currentDate;
        int notificationCount = slaEmailService.calculateMatchingSlaCount(threeMonthLater, oneMonthLater,date15Days, date5Days, date0Days);
        return ResponseEntity.ok(notificationCount);
    }

    @GetMapping("/sla/companyName")
    public ResponseEntity<List<String>> getSlaMatchingCompanyNames() {
        LocalDate currentDate = LocalDate.now();
        LocalDate threeMonthLater = currentDate.plusMonths(3);
        LocalDate oneMonthLater = currentDate.plusMonths(1);
        LocalDate date15Days = currentDate.plusDays(15);
        LocalDate date5Days = currentDate.plusDays(5);
        LocalDate date0Days = currentDate;
        List<String> companyNames = slaEmailService.getSlaMatchingCompanyNames(threeMonthLater, oneMonthLater,date15Days, date5Days, date0Days);
        return ResponseEntity.ok(companyNames);
    }
}
