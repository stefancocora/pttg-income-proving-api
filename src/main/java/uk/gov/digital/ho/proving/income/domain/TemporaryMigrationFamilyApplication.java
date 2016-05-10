package uk.gov.digital.ho.proving.income.domain;

import java.util.Date;

public class TemporaryMigrationFamilyApplication implements Application {
    private Individual individual;
    //    @JsonFormat(pattern="dd MMM yyyy")
    private Date applicationRaisedDate;

    private String category;

    public TemporaryMigrationFamilyApplication() {
    }

    public TemporaryMigrationFamilyApplication(Individual individual, Date applicationRaisedDate, String category, boolean meetsFinancialRequirements) {
        this.individual = individual;
        this.applicationRaisedDate = applicationRaisedDate;
        this.category = category;
    }

    public Individual getIndividual() {
        return individual;
    }

}
