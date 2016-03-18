package uk.gov.digital.ho.proving.income.domain;

import java.util.Date;

public interface Application {
    public Applicant getApplicant();
    public Date getApplicationDate();
    public String getCategory();
    public boolean isMeetsFinancialRequirements();
    public MonetaryAmount getThreshold();

}
