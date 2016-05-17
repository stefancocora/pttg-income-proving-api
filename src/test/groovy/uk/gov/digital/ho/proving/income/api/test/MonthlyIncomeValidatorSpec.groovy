package uk.gov.digital.ho.proving.income.api.test

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.api.FinancialCheckValues
import uk.gov.digital.ho.proving.income.api.IncomeValidator
import uk.gov.digital.ho.proving.income.domain.Individual
import uk.gov.digital.ho.proving.income.domain.Income

class MonthlyIncomeValidatorSpec extends Specification {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlyIncomeValidatorSpec.class);

    final String PIZZA_HUT = "Pizza Hut"
    final String BURGER_KING = "Burger King"

    int days = 182

    def "valid category A individual is accepted"() {

        given:
        List<Income> incomes = getConsecutiveIncomes()
        Date raisedDate = getDate(2015, Calendar.SEPTEMBER, 23)
        Date pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.MONTHLY_SALARIED_PASSED)

    }

    def "invalid category A individual is rejected (non consecutive)"() {

        given:
        List<Income> incomes = getNoneConsecutiveIncomes()
        Date raisedDate = getDate(2015, Calendar.SEPTEMBER, 23)
        Date pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.NON_CONSECUTIVE_MONTHS)

    }

    def "invalid category A individual is rejected (not enough records)"() {

        given:
        List<Income> incomes = getNotEnoughConsecutiveIncomes()
        Date raisedDate = getDate(2015, Calendar.SEPTEMBER, 23)
        Date pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.NOT_ENOUGH_RECORDS)

    }

    def "invalid category A individual is rejected (consecutive but not same employer)"() {

        given:
        List<Income> incomes = getConsecutiveIncomesButDifferentEmployers()
        Date raisedDate = getDate(2015, Calendar.SEPTEMBER, 23)
        Date pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.NON_CONSECUTIVE_MONTHS)

    }

    def "invalid category A individual is rejected (consecutive but not enough earnings)"() {

        given:
        List<Income> incomes = getConsecutiveIncomesButLowAmounts()
        Date raisedDate = getDate(2015, Calendar.SEPTEMBER, 23)
        Date pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.MONTHLY_VALUE_BELOW_THRESHOLD)

    }

    def "valid category A individual is accepted with different monthly pay dates"() {

        given:
        List<Income> incomes = getConsecutiveIncomesWithDifferentMonthlyPayDay()
        Date raisedDate = getDate(2015, Calendar.SEPTEMBER, 23)
        Date pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAMonthlySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.MONTHLY_SALARIED_PASSED)

    }

    def "valid category A individual is accepted with exactly the threshold values"() {

        given:
        List<Income> incomes = getConsecutiveIncomesWithExactlyTheAmount()
        Date raisedDate = getDate(2015, Calendar.SEPTEMBER, 23)
        Date pastDate = subtractDaysFromDate(raisedDate, days)

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
        incomes.add(new Income(getDate(2015, Calendar.JANUARY, 15), PIZZA_HUT, "1400"))
        incomes.add(new Income(getDate(2015, Calendar.MAY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.JUNE, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.JULY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.FEBRUARY, 15), BURGER_KING, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.AUGUST, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.SEPTEMBER, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getNotEnoughConsecutiveIncomes() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Calendar.MAY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.JUNE, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.JULY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.AUGUST, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getNoneConsecutiveIncomes() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Calendar.JANUARY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.FEBRUARY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.APRIL, 16), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.MAY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.JUNE, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.AUGUST, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.SEPTEMBER, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getConsecutiveIncomesButDifferentEmployers() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Calendar.JANUARY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.MAY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.JUNE, 15), BURGER_KING, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.JULY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.AUGUST, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.SEPTEMBER, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getConsecutiveIncomesButLowAmounts() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Calendar.JANUARY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.MAY, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.JUNE, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.JULY, 15), PIZZA_HUT, "1400"))
        incomes.add(new Income(getDate(2015, Calendar.AUGUST, 15), PIZZA_HUT, "1400"))
        incomes.add(new Income(getDate(2015, Calendar.SEPTEMBER, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getConsecutiveIncomesWithDifferentMonthlyPayDay() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Calendar.JANUARY, 15), PIZZA_HUT, "1400"))
        incomes.add(new Income(getDate(2015, Calendar.MAY, 16), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.JUNE, 17), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.APRIL, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.JULY, 14), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.FEBRUARY, 15), BURGER_KING, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.AUGUST, 15), PIZZA_HUT, "1600"))
        incomes.add(new Income(getDate(2015, Calendar.SEPTEMBER, 15), PIZZA_HUT, "1600"))
        incomes
    }

    def getConsecutiveIncomesWithExactlyTheAmount() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Calendar.JANUARY, 15), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Calendar.MAY, 16), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Calendar.JUNE, 17), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Calendar.APRIL, 15), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Calendar.JULY, 14), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Calendar.FEBRUARY, 15), BURGER_KING, "1550"))
        incomes.add(new Income(getDate(2015, Calendar.AUGUST, 15), PIZZA_HUT, "1550"))
        incomes.add(new Income(getDate(2015, Calendar.SEPTEMBER, 15), PIZZA_HUT, "1550"))
        incomes
    }

    Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        return cal.getTime()
    }


    Date subtractDaysFromDate(Date date, int days) {
        Calendar cal = Calendar.getInstance()
        cal.setTime(date)
        cal.add(Calendar.DAY_OF_YEAR, -days)
        return cal.getTime()
    }
}
