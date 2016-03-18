package uk.gov.digital.ho.proving.income.domain;

import java.util.Date;

public class TemporaryMigrationFamilyApplication implements Application {
    private Applicant applicant;
    //    @JsonFormat(pattern="dd MMM yyyy")
    private Date applicationReceivedDate;

    private String category;
    private FinancialRequirementsCheck financialRequirementsCheck;
    private MonetaryAmount threshold;

    public TemporaryMigrationFamilyApplication() {
    }

    public TemporaryMigrationFamilyApplication(Applicant applicant, Date applicationReceivedDate, String category, boolean meetsFinancialRequirements, FinancialRequirementsCheck financialRequirements, MonetaryAmount threshold) {
        this.applicant = applicant;
        this.applicationReceivedDate = applicationReceivedDate;
        this.category = category;
        this.financialRequirementsCheck = financialRequirements;
        this.threshold = threshold;
    }

    @Override
    public Applicant getApplicant() {
        return applicant;
    }

    @Override
    public Date getApplicationReceivedDate() {
        return applicationReceivedDate;
    }

    @Override
    public void setApplicationReceivedDate(Date applicationReceivedDate) {
        this.applicationReceivedDate = applicationReceivedDate;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public FinancialRequirementsCheck getFinancialRequirementsCheck() {
        return financialRequirementsCheck;
    }

    @Override
    public MonetaryAmount getThreshold() {
        return threshold;
    }

}
