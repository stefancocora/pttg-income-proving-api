package uk.gov.digital.ho.proving.income.service;

import uk.gov.digital.ho.proving.income.domain.Application;

public class TemporaryMigrationFamilyCaseworkerApplicationResponse {
    private Application application;
    private ValidationError error;

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setError(ValidationError error) {
        this.error = error;
    }

    public Application getApplication() {
        return application;
    }

    public ValidationError getError() {
        return error;
    }
}
