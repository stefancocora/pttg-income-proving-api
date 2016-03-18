package uk.gov.digital.ho.proving.income.domain;

import java.util.Date;

public interface Application {
    public Applicant getApplicant();
    public Date getApplicationReceivedDate();
    public void setApplicationReceivedDate(Date applicationReceivedDate);
    public String getCategory();
    public FinancialRequirementsCheck getFinancialRequirementsCheck();
    public MonetaryAmount getThreshold();

}
