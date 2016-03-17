package uk.gov.digital.ho.proving.income.api;

public class ValidationError {
    private String code;
    private String description;

    public ValidationError(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
