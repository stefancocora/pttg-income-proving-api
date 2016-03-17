package uk.gov.digital.ho.proving.income.domain;

import java.util.Date;

public class TemporaryMigrationFamilyApplication implements Application {
    private Applicant applicant;
//    @JsonFormat(pattern="dd MMM yyyy")
    private Date applicationDate;

    private String category;
    private boolean meetsFinancialRequirements;
    private MonetaryAmount threshold;

    public TemporaryMigrationFamilyApplication() {
    }

    public TemporaryMigrationFamilyApplication(Applicant applicant, Date applicationDate, String category, boolean meetsFinancialRequirements, MonetaryAmount threshold) {
        this.applicant = applicant;
        this.applicationDate = applicationDate;
        this.category = category;
        this.meetsFinancialRequirements = meetsFinancialRequirements;
        this.threshold = threshold;
    }

    @Override
    public Applicant getApplicant() {
        return applicant;
    }

    @Override
    public Date getApplicationDate() {
        return applicationDate;
    }

    public String getCategory() {
        return category;
    }

    public boolean isMeetsFinancialRequirements() {
        return meetsFinancialRequirements;
    }

    public MonetaryAmount getThreshold() {
        return threshold;
    }

}
