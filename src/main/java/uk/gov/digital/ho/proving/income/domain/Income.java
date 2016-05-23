package uk.gov.digital.ho.proving.income.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Income {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyy-MM-dd", timezone = "GMT")
    private Date payDate;
    private String employer;
    private String income;

    public Income() {
    }

    public Income(Date payDate, String employer, String income) {
        this.payDate = payDate;
        this.employer = employer;
        this.income = income;
    }

    public Date getPayDate() {
        return new Date(payDate.getTime());
    }

    public void setPayDate(Date payDate) {
        this.payDate = new Date(payDate.getTime());
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "Income{" +
                "payDate='" + payDate + '\'' +
                ", employer='" + employer + '\'' +
                ", income='" + income + '\'' +
                '}';
    }

}
