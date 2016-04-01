package uk.gov.digital.ho.proving.income.domain;

import java.util.List;

// TODO rename this class
public class IncomeProvingResponse {
    private Applicant applicant;
    private List<Income> incomes;
    private List<Link> links;
    private String total;


    public IncomeProvingResponse() {
    }

    public IncomeProvingResponse(Applicant applicant, List<Income> incomes, List<Link> links, String total) {
        this.applicant = applicant;
        this.incomes = incomes;
        this.links = links;
        this.total = total;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<Income> incomes) {
        this.incomes = incomes;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "IncomeProvingResponse{" +
                "applicant=" + applicant +
                ", incomes=" + incomes +
                ", links=" + links +
                ", total='" + total + '\'' +
                '}';
    }
}
