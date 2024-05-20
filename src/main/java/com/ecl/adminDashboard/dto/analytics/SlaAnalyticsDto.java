package com.ecl.adminDashboard.dto.analytics;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SlaAnalyticsDto {
    private Integer slaCount;

    private Integer activeSLA;

    private Integer expiredSLA;

    private Integer renewedSLA;

//    public SlaAnalyticsDto(Integer slaCount){
//
//        this.slaCount = slaCount;
//    }

}
