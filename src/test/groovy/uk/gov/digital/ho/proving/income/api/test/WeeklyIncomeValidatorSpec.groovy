package uk.gov.digital.ho.proving.income.api.test

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.api.FinancialCheckValues
import uk.gov.digital.ho.proving.income.api.IncomeValidator
import uk.gov.digital.ho.proving.income.domain.Individual
import uk.gov.digital.ho.proving.income.domain.Income

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
        Date raisedDate = getDate(2015, Calendar.AUGUST, 16)
        Date pastDate = subtractDaysFromDate(raisedDate, days)


        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAWeeklySalaried(incomes, pastDate, raisedDate , 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.WEEKLY_SALARIED_PASSED)

    }


    def "valid category A individual is accepted with exactly 26 records"() {

        given:
        List<Income> incomes = getIncomesExactly26AboveThreshold()

        Date raisedDate = getDate(2015, Calendar.AUGUST, 16)
        Date pastDate = subtractDaysFromDate(raisedDate, days)

        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAWeeklySalaried(incomes, pastDate, raisedDate , 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.WEEKLY_SALARIED_PASSED)

    }

    def "invalid category A individual is rejected with exactly 26 records as raised date is before last payday"() {

        given:
        List<Income> incomes = getIncomesExactly26AboveThreshold()
        Date raisedDate = getDate(2015, Calendar.AUGUST, 10)
        Date pastDate = subtractDaysFromDate(raisedDate, days)


        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAWeeklySalaried(incomes, pastDate, raisedDate , 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.NOT_ENOUGH_RECORDS)

    }


    def "invalid category A not enough weekly data"() {

        given:
        List<Income> incomes = getIncomesNotEnoughWeeks();
        Date raisedDate = getDate(2015, Calendar.AUGUST, 16)
        Date pastDate = subtractDaysFromDate(raisedDate, days)


        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAWeeklySalaried(incomes, pastDate, raisedDate , 0)

        then:
        categoryAIndividual.equals(FinancialCheckValues.NOT_ENOUGH_RECORDS)

    }

    def "invalid category A some weeks below threshold"() {

        given:
        List<Income> incomes = getIncomesSomeBelowThreshold();
        Date raisedDate = getDate(2015, Calendar.AUGUST, 16)
        Date pastDate = subtractDaysFromDate(raisedDate, days)


        when:
        FinancialCheckValues categoryAIndividual = IncomeValidator.validateCategoryAWeeklySalaried(incomes, pastDate, raisedDate , 0)

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
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,11), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,4), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,30), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,23), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,16), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,9), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,2), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,26), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,19), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,12), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,5), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,31), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JANUARY,27), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JANUARY,20), PIZZA_HUT, aboveThreshold))
        incomes
    }

    def getIncomesOnTheThreshold() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,11), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,4), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,28), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,21), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,14), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,7), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,30), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,23), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,16), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,9), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,2), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,26), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,19), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,12), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,5), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,28), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,21), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,14), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,7), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,31), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,24), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,17), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,10), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,3), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,24), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,17), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,10), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,3), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JANUARY,27), PIZZA_HUT, weeklyThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JANUARY,20), PIZZA_HUT, weeklyThreshold))
        incomes
    }


    def getIncomesBelowThreshold() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,11), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,4), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,30), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,23), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,16), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,9), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,2), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,26), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,19), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,12), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,5), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,31), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JANUARY,27), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JANUARY,20), PIZZA_HUT, aboveThreshold))
        incomes
    }


    def getIncomesExactly26AboveThreshold() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,11), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,4), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,30), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,23), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,16), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,9), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,2), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,26), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,19), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,12), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,5), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,31), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,17), PIZZA_HUT, aboveThreshold))

        incomes
    }


    def getIncomesNotEnoughWeeks() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,11), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,4), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,30), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,23), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,16), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,9), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Calendar.JUNE,2), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Calendar.MAY,26), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Calendar.MAY,19), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Calendar.MAY,12), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Calendar.MAY,5), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Calendar.APRIL,28), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Calendar.APRIL,21), PIZZA_HUT, aboveThreshold))
//        incomes.add(new Income(getDate(2015,Calendar.APRIL,14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,31), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JANUARY,27), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JANUARY,20), PIZZA_HUT, aboveThreshold))

        incomes
    }


    def getIncomesSomeBelowThreshold() {
        List<Income> incomes = new ArrayList()
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,11), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.AUGUST,4), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JULY,7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,30), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,23), PIZZA_HUT, belowThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,16), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,9), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JUNE,2), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,26), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,19), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,12), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MAY,5), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,28), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,21), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,14), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.APRIL,7), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,31), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.MARCH,3), PIZZA_HUT, belowThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,24), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,17), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,10), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.FEBRUARY,3), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JANUARY,27), PIZZA_HUT, aboveThreshold))
        incomes.add(new Income(getDate(2015,Calendar.JANUARY,20), PIZZA_HUT, aboveThreshold))
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
