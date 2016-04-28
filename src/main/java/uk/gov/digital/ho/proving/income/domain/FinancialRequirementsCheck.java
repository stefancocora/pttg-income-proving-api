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
    private Date applicationRaisedDate;

    private String failureReason;

    public Date getApplicationRaisedDate() {
        return applicationRaisedDate;
    }

    public void setApplicationRaisedDate(Date applicationRaisedDate) {
        this.applicationRaisedDate = applicationRaisedDate;
    }

    public FinancialRequirementsCheck() {
    }

    public Date getCheckedFrom() {
        Date referenceDate = applicationRaisedDate;
        Calendar c = Calendar.getInstance();
        c.setTime(referenceDate);
        c.add(Calendar.MONTH, -6);
        return c.getTime();
    }

    public Date getCheckedTo() {
        return applicationRaisedDate;
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
