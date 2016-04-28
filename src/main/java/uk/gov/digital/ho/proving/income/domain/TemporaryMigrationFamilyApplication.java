package uk.gov.digital.ho.proving.income.domain;

import java.util.Date;

public class TemporaryMigrationFamilyApplication implements Application {
    private Applicant applicant;
    //    @JsonFormat(pattern="dd MMM yyyy")
    private Date applicationRaisedDate;

    private String category;
    private FinancialRequirementsCheck financialRequirementsCheck;
    private MonetaryAmount threshold;

    public TemporaryMigrationFamilyApplication() {
    }

    public TemporaryMigrationFamilyApplication(Applicant applicant, Date applicationRaisedDate, String category, boolean meetsFinancialRequirements, FinancialRequirementsCheck financialRequirements, MonetaryAmount threshold) {
        this.applicant = applicant;
        this.applicationRaisedDate = applicationRaisedDate;
        this.category = category;
        this.financialRequirementsCheck = financialRequirements;
        this.threshold = threshold;
    }

    @Override
    public Applicant getApplicant() {
        return applicant;
    }

    @Override
    public Date getApplicationRaisedDate() {
        return applicationRaisedDate;
    }

    @Override
    public void setApplicationRaisedDate(Date applicationRaisedDate) {
        this.applicationRaisedDate = applicationRaisedDate;
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
