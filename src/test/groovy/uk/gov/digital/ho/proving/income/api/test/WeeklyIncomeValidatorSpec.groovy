package uk.gov.digital.ho.proving.income.api.test

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.api.FinancialCheckValues
import uk.gov.digital.ho.proving.income.api.IncomeValidator
import uk.gov.digital.ho.proving.income.domain.Individual
import uk.gov.digital.ho.proving.income.domain.Income

import java.time.LocalDate
import java.time.Month

class WeeklyIncomeValidatorSpec extends Specification {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeeklyIncomeValidatorSpec.class);

    final String PIZZA_HUT = "Pizza Hut"
    final String BURGER_KING = "Burger King"


    String weeklyThreshold = "357.69"
    String aboveThreshold = "357.70"
    String belowThreshold = "357.68"

    int days = 182

    def "valid category A individual is accepted"() {

        given:
        List<Income> incomes = getIncomesAboveThreshold()
        LocalDate raisedDate = getDate(2015, Month.AUGUST, 16)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)


        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAWeeklySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.WEEKLY_SALARIED_PASSED)

    }


    def "valid category A individual is accepted with exactly 26 records"() {

        given:
        List<Income> incomes = getIncomesExactly26AboveThreshold()

        LocalDate raisedDate = getDate(2015, Month.AUGUST, 16)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAWeeklySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.WEEKLY_SALARIED_PASSED)

    }

    def "invalid category A individual is rejected with exactly 26 records as raisedLocalDate is before last payday"() {

        given:
        List<Income> incomes = getIncomesExactly26AboveThreshold()
        LocalDate raisedDate = getDate(2015, Month.AUGUST, 10)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)


        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAWeeklySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.NOT_ENOUGH_RECORDS)

    }


    def "invalid category A not enough weekly data"() {

        given:
        List<Income> incomes = getIncomesNotEnoughWeeks();
        LocalDate raisedDate = getDate(2015, Month.AUGUST, 16)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)


        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAWeeklySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.NOT_ENOUGH_RECORDS)

    }

    def "invalid category A some weeks below threshold"() {

        given:
        List<Income> incomes = getIncomesSomeBelowThreshold();
        LocalDate raisedDate = getDate(2015, Month.AUGUST, 16)
        LocalDate pastDate = subtractDaysFromDate(raisedDate, days)


        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAWeeklySalaried(incomes, pastDate, raisedDate, 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.WEEKLY_VALUE_BELOW_THRESHOLD)

    }


    def getIndividual() {
        Individual individual = new Individual()
        individual.title = "Mr"
        individual.forename = "Duncan"
        individual.surname = "Sinclair"
        individual.nino = "AA123456A"
        individual
    }

    def getIncomesAboveThreshold() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.AUGUST, 11), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 4), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 30), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 23), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 16), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 9), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 2), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 26), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 19), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 12), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 5), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 31), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JANUARY, 27), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JANUARY, 20), PIZZA_HUT, aboveThreshold))
        incomes
    }

    def getIncomesOnTheThreshold() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.AUGUST, 11), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 4), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 28), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 21), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 14), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 7), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 30), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 23), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 16), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 9), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 2), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 26), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 19), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 12), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 5), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 28), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 21), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 14), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 7), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 31), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 24), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 17), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 10), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 3), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 24), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 17), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 10), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 3), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.JANUARY, 27), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015, Month.JANUARY, 20), PIZZA_HUT, weeklyThreshold))
        incomes
    }


    def getIncomesBelowThreshold() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.AUGUST, 11), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 4), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 30), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 23), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 16), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 9), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 2), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 26), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 19), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 12), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 5), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 31), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JANUARY, 27), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JANUARY, 20), PIZZA_HUT, aboveThreshold))
        incomes
    }


    def getIncomesExactly26AboveThreshold() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.AUGUST, 11), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 4), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 30), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 23), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 16), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 9), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 2), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 26), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 19), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 12), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 5), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 31), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 17), PIZZA_HUT, aboveThreshold))

        incomes
    }


    def getIncomesNotEnoughWeeks() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.AUGUST, 11), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 4), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 30), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 23), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 16), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 9), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Month.JUNE,2), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Month.MAY,26), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Month.MAY,19), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Month.MAY,12), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Month.MAY,5), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Month.APRIL,28), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Month.APRIL,21), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Month.APRIL,14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 31), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JANUARY, 27), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JANUARY, 20), PIZZA_HUT, aboveThreshold))

        incomes
    }


    def getIncomesSomeBelowThreshold() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015, Month.AUGUST, 11), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.AUGUST, 4), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JULY, 7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 30), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 23), PIZZA_HUT, belowThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 16), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 9), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JUNE, 2), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 26), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 19), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 12), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MAY, 5), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.APRIL, 7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 31), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.MARCH, 3), PIZZA_HUT, belowThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.FEBRUARY, 3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JANUARY, 27), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015, Month.JANUARY, 20), PIZZA_HUT, aboveThreshold))
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
