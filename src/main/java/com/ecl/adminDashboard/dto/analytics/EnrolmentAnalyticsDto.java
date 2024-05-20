package com.ecl.adminDashboard.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EnrolmentAnalyticsDto {
    private Integer enrolmentCount;

    private Integer activeEnrolments;

    private Integer expiredEnrolments;

    private Integer renewedEnrolments;

//    public EnrolmentAnalyticsDto(Integer enrolmentCount){
//        this.enrolmentCount = enrolmentCount;
//    }


}
