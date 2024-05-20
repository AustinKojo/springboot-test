package com.ecl.adminDashboard.dto.analytics;

import java.util.Date;

public class DateRangeRequestDto {
    private Date startDate;
    private Date endDate;

    // Getters and setters
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
