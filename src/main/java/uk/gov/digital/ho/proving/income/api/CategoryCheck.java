package uk.gov.digital.ho.proving.income.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CategoryCheck {

    private String category;
    private boolean passed;

    // @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="GMT")
    private Date applicationRaisedDate;

    // @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="GMT")
    private Date assessmentStartDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FinancialCheckValues failureReason;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public CategoryCheck(String category, boolean passed, FinancialCheckValues failureReason, Date applicationRaisedDate, Date assessmentStartDate) {
        this.category = category;
        this.passed = passed;
        this.applicationRaisedDate = applicationRaisedDate;
        this.assessmentStartDate = assessmentStartDate;
        this.failureReason = failureReason;
    }

    private String formatDate(Date date){
        return sdf.format(date);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getApplicationRaisedDate() {
        return formatDate(applicationRaisedDate);
    }

    public void setApplicationRaisedDate(Date applicationRaisedDate) {
        this.applicationRaisedDate = applicationRaisedDate;
    }

    public String getAssessmentStartDate() {
        return formatDate(assessmentStartDate);
    }

    public void setAssessmentStartDate(Date assessmentStartDate) {
        this.assessmentStartDate = assessmentStartDate;
    }

    public FinancialCheckValues getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(FinancialCheckValues failureReason) {
        this.failureReason = failureReason;
    }
}
