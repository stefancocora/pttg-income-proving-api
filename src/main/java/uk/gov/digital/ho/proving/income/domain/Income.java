package uk.gov.digital.ho.proving.income.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Income {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private LocalDate payDate;
    private String employer;
    private String income;

    public Income() {
    }

    public Income(LocalDate payDate, String employer, String income) {
        this.payDate = payDate;
        this.employer = employer;
        this.income = income;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
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
