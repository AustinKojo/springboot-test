package com.ecl.adminDashboard.service.Analytics;

import com.ecl.adminDashboard.dto.analytics.*;

import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.repository.CustomerRepository;
import com.ecl.adminDashboard.repository.EnrolmentRepository;
import com.ecl.adminDashboard.repository.ProductRepository;
import com.ecl.adminDashboard.repository.SLARepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AnalyticsImpl implements AnalyticsService{

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private EnrolmentRepository enrolmentRepository;

    private SLARepository slaRepository;


        @Override
        public AnalyticsDto getAnalytics() {
            AnalyticsDto resp = new AnalyticsDto();
           // AnalyticsDto analyticsDto = new AnalyticsDto();

            int customerCount  =  customerRepository.getCustomerCountByState("active");
            int productCount = productRepository.getProductCountByState("active");
            int enrolmentCount = enrolmentRepository.getEnrolmentCount();
            int slaCount = slaRepository.getSLACount();
            int activeEnrolmentCount = enrolmentRepository.getEnrolmentCountByStatus("ACTIVE");
            int expiredEnrolmentsCount = enrolmentRepository.getEnrolmentCountByStatus("EXPIRED");
            int renewedEnrolmentCount = enrolmentRepository.getEnrolmentCountByStatus("RENEWED");
            int activeSLACount = slaRepository.getSLACountByStatus("ACTIVE");
            int expiredSLACount = slaRepository.getSLACountByStatus("EXPIRED");
            int renewedSLACount = slaRepository.getSLACountByStatus("RENEWED");


            resp.setCustomerAnalyticsDto(new CustomerAnalyticsDto(customerCount));
            resp.setProductAnalyticsDto(new ProductAnalyticsDto((productCount)));
            resp.setEnrolmentAnalyticsDto(new EnrolmentAnalyticsDto(enrolmentCount, activeEnrolmentCount, expiredEnrolmentsCount, renewedEnrolmentCount));
            resp.setSlaAnalyticsDto(new SlaAnalyticsDto(slaCount, activeSLACount, expiredSLACount, renewedSLACount));

            resp.setResponseCode("000");
            resp.setResponseMessage("Success");

            return resp;
        }

//    @Autowired
//    public AnalyticsImpl(EnrolmentRepository enrolmentRepository) {
//        this.enrolmentRepository = enrolmentRepository;
//    }

    public List<CustomerProductCountDto> getCustomerProductCounts() {
        List<Object[]> result = enrolmentRepository.getCustomerProductCount();
        return mapToCustomerProductCountDtos(result);
    }

    private List<CustomerProductCountDto> mapToCustomerProductCountDtos(List<Object[]> result) {
        // Convert the result to a list of CustomerProductCountDto
        // You can use stream and map to perform the conversion
        return result.stream()
                .map(row -> {
                    if (row[0] instanceof String && row[1] instanceof Long) {
                        return new CustomerProductCountDto((String) row[0], (Long) row[1]);
                    } else {
                        // Handle unexpected types or null values, throw an exception or log a warning
                        // Alternatively, you can return a default value or skip the entry
                        throw new IllegalStateException("Unexpected types in the result row");
                    }
                })
                .collect(Collectors.toList());
    }


    public List<ProductCustomerCountDTO> getProductCustomerCounts() {
        List<Object[]> result = enrolmentRepository.getProductCustomerCount();
        return mapToProductCustomerCountDtos(result);
    }

    private List<ProductCustomerCountDTO> mapToProductCustomerCountDtos(List<Object[]> result) {
        // Convert the result to a list of CustomerProductCountDto
        // You can use stream and map to perform the conversion
        return result.stream()
                .map(row -> {
                    if (row[0] instanceof String && row[1] instanceof Long) {
                        return new ProductCustomerCountDTO((String) row[0], (Long) row[1]);
                    } else {
                        // Handle unexpected types or null values, throw an exception or log a warning
                        // Alternatively, you can return a default value or skip the entry
                        throw new IllegalStateException("Unexpected types in the result row");
                    }
                })
                .collect(Collectors.toList());
    }

    public List<SlaCustomerProductCountDto> getSlaCustomerProductCounts() {
        List<Object[]> result = slaRepository.getSlaCustomerProductCount();
        return mapToSlaCustomerProductCountDtos(result);
    }

    private List<SlaCustomerProductCountDto> mapToSlaCustomerProductCountDtos(List<Object[]> result) {
        // Convert the result to a list of CustomerProductCountDto
        // You can use stream and map to perform the conversion
        return result.stream()
                .map(row -> {
                    if (row[0] instanceof String && row[1] instanceof Long) {
                        return new SlaCustomerProductCountDto((String) row[0], (Long) row[1]);
                    } else {
                        // Handle unexpected types or null values, throw an exception or log a warning
                        // Alternatively, you can return a default value or skip the entry
                        throw new IllegalStateException("Unexpected types in the result row");
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SlaCustomerProductCountDto> getSlaCustomerProductCountsByDateRange(DateRangeRequestDto dateRange) {
        Date startDate = dateRange.getStartDate();
        Date endDate = dateRange.getEndDate();
        List<Object[]> result = slaRepository.getSlaCustomerProductCountByDateRange(startDate, endDate);

        return result.stream()
                .map(row -> {
                    if (row[0] instanceof String && row[1] instanceof Long) {
                        return new SlaCustomerProductCountDto((String) row[0], (Long) row[1]);
                    } else {
                        throw new IllegalStateException("Unexpected types in the result row");
                    }
                })
                .collect(Collectors.toList());    }

//    @Override
//    public AnalyticsDto getAnalytics() {
//        ResponseObjectAnalytics resp = new ResponseObjectAnalytics();
//        AnalyticsDto analyticsDto = new AnalyticsDto();
//
//        int customerCount  =  customerRepository.getCustomerCount();
//        int productCount = productRepository.getProductCount();
//        int enrolmentCount = enrolmentRepository.getEnrolmentCount();
//        int activeEnrolmentCount = enrolmentRepository.getEnrolmentCountByStatus("ACTIVE");
//        int expiredEnrolmentsCount = enrolmentRepository.getEnrolmentCountByStatus("EXPIRED");
//        int renewedEnrolmentCount = enrolmentRepository.getEnrolmentCountByStatus("RENEWED");
//
//        analyticsDto.setCustomerAnalyticsDto(new CustomerAnalyticsDto(customerCount));
//        analyticsDto.setProductAnalyticsDto(new ProductAnalyticsDto((productCount)));
//        analyticsDto.setEnrolmentAnalyticsDto(new EnrolmentAnalyticsDto(enrolmentCount, activeEnrolmentCount, expiredEnrolmentsCount, renewedEnrolmentCount));
//
//        analyticsDto.setResponseCode("000");
//        analyticsDto.setResponseMessage("Success");
//        analyticsDto.setObject(analyticsDto);
//
//        return resp;
//    }
}
