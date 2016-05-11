package uk.gov.digital.ho.proving.income.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import uk.gov.digital.ho.proving.income.domain.Individual;


public class TemporaryMigrationFamilyCaseworkerApplicationResponse {

    @JsonInclude(Include.NON_NULL)
    private Individual individual;

    @JsonInclude(Include.NON_NULL)
    private CategoryCheck categoryCheck;

    private ResponseStatus status;

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public CategoryCheck getCategoryCheck() {
        return categoryCheck;
    }

    public void setCategoryCheck(CategoryCheck categoryCheck) {
        this.categoryCheck = categoryCheck;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }
}
