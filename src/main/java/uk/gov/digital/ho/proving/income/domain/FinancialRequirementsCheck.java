package uk.gov.digital.ho.proving.income.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by andrewmoores on 18/03/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinancialRequirementsCheck {
    private Boolean met;
    private Date checkedFrom;
    private Date checkedTo;
    @JsonIgnore
    private Date applicationReceivedDate;

    private String failureReason;

    public Date getApplicationReceivedDate() {
        return applicationReceivedDate;
    }

    public void setApplicationReceivedDate(Date applicationReceivedDate) {
        this.applicationReceivedDate = applicationReceivedDate;
    }

    public FinancialRequirementsCheck() {
    }

    public Date getCheckedFrom() {
        Date referenceDate = applicationReceivedDate;
        Calendar c = Calendar.getInstance();
        c.setTime(referenceDate);
        c.add(Calendar.MONTH, -6);
        return c.getTime();
    }

    public Date getCheckedTo() {
        return applicationReceivedDate;
    }

    public Boolean getMet() {
        return met;
    }

    public void setMet(Boolean met) {
        this.met = met;
    }

    public void setCheckedFrom(Date checkedFrom) {
        this.checkedFrom = checkedFrom;
    }

    public void setCheckedTo(Date checkedTo) {
        this.checkedTo = checkedTo;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
