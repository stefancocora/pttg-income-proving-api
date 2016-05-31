package uk.gov.digital.ho.proving.income.api.test

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.api.FinancialCheckValues
import uk.gov.digital.ho.proving.income.api.IncomeValidator
import uk.gov.digital.ho.proving.income.domain.Income
import uk.gov.digital.ho.proving.income.domain.Individual

import java.time.LocalDate
import java.time.Month

class MonthlyIncomeValidatorSpec extends Specification {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlyIncomeValidatorSpec.class);

    final String PIZZA_HUT = "Pizza Hut"
    final String BURGER_KING = "Burger King"

    int days = 182

    def "valid category A individual is accepted"() {

        given:
        List<Income> incomes = getConsecutiveIncomes()
        LocalDate raisedDate = getDate(2015, Month.SEPTEMBER, 23)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.MONTHLY_SALARIED_PASSED)

    }

    def "invalid category A individual is rejected (non consecutive)"() {

        given:
        List<Income> incomes = getNoneConsecutiveIncomes()
        LocalDate raisedDate = getDate(2015, Month.SEPTEMBER, 23)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.NON_CONSECUTIVE_MONTHS)

    }

    def "invalid category A individual is rejected (not enough records)"() {

        given:
        List<Income> incomes = getNotEnoughConsecutiveIncomes()
        LocalDate raisedDate = getDate(2015, Month.SEPTEMBER, 23)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.NOT_ENOUGH_RECORDS)

    }

    def "invalid category A individual is rejected (consecutive but not same employer)"() {

        given:
        List<Income> incomes = getConsecutiveIncomesButDifferentEmployers()
        LocalDate raisedDate = getDate(2015, Month.SEPTEMBER, 23)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.NON_CONSECUTIVE_MONTHS)

    }

    def "invalid category A individual is rejected (consecutive but not enough earnings)"() {

        given:
        List<Income> incomes = getConsecutiveIncomesButLowAmounts()
        LocalDate raisedDate = getDate(2015, Month.SEPTEMBER, 23)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.MONTHLY_VALUE_BELOW_THRESHOLD)

    }

    def "valid category A individual is accepted with different monthly payLocalDates"() {

        given:
        List<Income> incomes = getConsecutiveIncomesWithDifferentMonthlyPayDay()
        LocalDate raisedDate = getDate(2015, Month.SEPTEMBER, 23)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.MONTHLY_SALARIED_PASSED)

    }

    def "valid category A individual is accepted with exactly the threshold values"() {

        given:
        List<Income> incomes = getConsecutiveIncomesWithExactlyTheAmount()
        LocalDate raisedDate = getDate(2015, Month.SEPTEMBER, 23)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.MONTHLY_SALARIED_PASSED)

    }


    def getIndividual() {
        Individual individual = new Individual()
        individual.title = "Mr"
        individual.forename = "Duncan"
        individual.surname = "Sinclair"
        individual.nino = "AA123456A"
        individual
    }

    def getConsecutiveIncomes() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.JANUARY, 15), PIZZA_HUT, "1400"))
        incomes.add(new Income(getDate(2015, Month.MAY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.JUNE, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.JULY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 15), BURGER_KING, "1600"))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.SEPTEMBER, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getNotEnoughConsecutiveIncomes() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.MAY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.JUNE, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.JULY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getNoneConsecutiveIncomes() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.JANUARY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.APRIL, 16), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.MAY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.JUNE, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.SEPTEMBER, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getConsecutiveIncomesButDifferentEmployers() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.JANUARY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.MAY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.JUNE, 15), BURGER_KING, "1600"))
        incomes.add(new Income(getDate(2015, Month.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.JULY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.SEPTEMBER, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getConsecutiveIncomesButLowAmounts() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.JANUARY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.MAY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.JUNE, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.JULY, 15), PIZZA_HUT, "1400"))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 15), PIZZA_HUT, "1400"))
        incomes.add(new Income(getDate(2015, Month.SEPTEMBER, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getConsecutiveIncomesWithDifferentMonthlyPayDay() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.JANUARY, 15), PIZZA_HUT, "1400"))
        incomes.add(new Income(getDate(2015, Month.MAY, 16), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.JUNE, 17), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.JULY, 14), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 15), BURGER_KING, "1600"))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Month.SEPTEMBER, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getConsecutiveIncomesWithExactlyTheAmount() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.JANUARY, 15), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Month.MAY, 16), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Month.JUNE, 17), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Month.APRIL, 15), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Month.JULY, 14), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 15), BURGER_KING, "1550"))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 15), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Month.SEPTEMBER, 15), PIZZA_HUT, "1550"))
        incomes
    }

    LocalDate getDate(int year, Month month, int day) {
        LocalDate localDate = LocalDate.of(year,month,day)
        return localDate
    }


    LocalDate subtractDaysFromDate(LocalDate date, long days) {
        return date.minusDays(days);
    }
}
