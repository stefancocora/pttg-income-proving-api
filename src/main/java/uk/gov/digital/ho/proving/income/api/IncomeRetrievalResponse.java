package uk.gov.digital.ho.proving.income.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import uk.gov.digital.ho.proving.income.domain.Income;
import uk.gov.digital.ho.proving.income.domain.Individual;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

public class IncomeRetrievalResponse extends BaseResponse {

    @JsonInclude(Include.NON_NULL)
    private Individual individual;

    @JsonInclude(Include.NON_NULL)
    List<Income> incomes;

    public String getTotal() {
        Stream<BigDecimal> decimalValues = incomes.stream().map (income -> new BigDecimal(income.getIncome()));
        BigDecimal total = decimalValues.reduce(BigDecimal.ZERO, ( sum, value ) -> sum.add(value));

        return total.toString();
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<Income> incomes) {
        this.incomes = incomes;
    }

}
