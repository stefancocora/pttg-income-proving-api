package uk.gov.digital.ho.proving.income.domain;

public class Applicant {
    private String title;
    private String forename;
    private String surname;
    private String nino;

    public Applicant(String title, String forename, String surname, String nino) {
        this.title = title;
        this.forename = forename;
        this.surname = surname;
        this.nino = nino;
    }

    public Applicant() {
    }

    public String getTitle() {
        return title;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }

    public String getNino() {
        return nino;
    }
}
