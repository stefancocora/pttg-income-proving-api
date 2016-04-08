package uk.gov.digital.ho.proving.income.api.test

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.api.FinancialCheckValues
import uk.gov.digital.ho.proving.income.api.IncomeValidator
import uk.gov.digital.ho.proving.income.domain.Applicant
import uk.gov.digital.ho.proving.income.domain.Income
import uk.gov.digital.ho.proving.income.domain.IncomeProvingResponse
import uk.gov.digital.ho.proving.income.domain.Link

class IncomeValidatorSpec extends Specification {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomeValidatorSpec.class);

    final String PIZZA_HUT = "Pizza Hut"
    final String BURGER_KING = "Burger King"

    def "valid category A applicant is accepted"() {

        given:
        Applicant applicant = getApplicant()
        List<Income> incomes = getConsecutiveIncomes()
        IncomeProvingResponse incomeProvingResponse = new IncomeProvingResponse(applicant, incomes, new ArrayList<Link>(), "9600")

        when:
        FinancialCheckValues categoryAApplicant = IncomeValidator.validateCategoryAApplicant(incomeProvingResponse, getDate(2015, Calendar.MARCH, 23), getDate(2015, Calendar.SEPTEMBER, 23))

        then:
        categoryAApplicant.equals(FinancialCheckValues.PASSED)

    }

    def "invalid category A applicant is rejected (non consecutive)"() {

        given:
        Applicant applicant = getApplicant()
        List<Income> incomes = getNoneConsecutiveIncomes()
        IncomeProvingResponse incomeProvingResponse = new IncomeProvingResponse(applicant, incomes, new ArrayList<Link>(), "9600")

        when:
        FinancialCheckValues categoryAApplicant = IncomeValidator.validateCategoryAApplicant(incomeProvingResponse, getDate(2015, Calendar.MARCH, 23), getDate(2015, Calendar.SEPTEMBER, 23))

        then:
        categoryAApplicant.equals(FinancialCheckValues.NON_CONSECUTIVE_MONTHS)

    }

    def "invalid category A applicant is rejected (not enough records)"() {

        given:
        Applicant applicant = getApplicant()
        List<Income> incomes = getNotEnoughConsecutiveIncomes()
        IncomeProvingResponse incomeProvingResponse = new IncomeProvingResponse(applicant, incomes, new ArrayList<Link>(), "9600")

        when:
        FinancialCheckValues categoryAApplicant = IncomeValidator.validateCategoryAApplicant(incomeProvingResponse, getDate(2015, Calendar.MARCH, 23), getDate(2015, Calendar.SEPTEMBER, 23))

        then:
        categoryAApplicant.equals(FinancialCheckValues.NOT_ENOUGH_RECORDS)

    }

    def "invalid category A applicant is rejected (consecutive but not same employer)"() {

        given:
        Applicant applicant = getApplicant()
        List<Income> incomes = getConsecutiveIncomesButDifferentEmployers()
        IncomeProvingResponse incomeProvingResponse = new IncomeProvingResponse(applicant, incomes, new ArrayList<Link>(), "9600")

        when:
        FinancialCheckValues categoryAApplicant = IncomeValidator.validateCategoryAApplicant(incomeProvingResponse, getDate(2015, Calendar.MARCH, 23), getDate(2015, Calendar.SEPTEMBER, 23))

        then:
        categoryAApplicant.equals(FinancialCheckValues.NON_CONSECUTIVE_MONTHS)

    }

    def "invalid category A applicant is rejected (consecutive but not enough earnings)"() {

        given:
        Applicant applicant = getApplicant()
        List<Income> incomes = getConsecutiveIncomesButLowAmounts()
        IncomeProvingResponse incomeProvingResponse = new IncomeProvingResponse(applicant, incomes, new ArrayList<Link>(), "9600")

        when:
        FinancialCheckValues categoryAApplicant = IncomeValidator.validateCategoryAApplicant(incomeProvingResponse, getDate(2015, Calendar.MARCH, 23), getDate(2015, Calendar.SEPTEMBER, 23))

        then:
        categoryAApplicant.equals(FinancialCheckValues.MONTHLY_VALUE_BELOW_THRESHOLD)

    }

    def "valid category A applicant is accepted with different monthly pay dates"() {

        given:
        Applicant applicant = getApplicant()
        List<Income> incomes = getConsecutiveIncomesWithDifferentMonthlyPayDay()
        IncomeProvingResponse incomeProvingResponse = new IncomeProvingResponse(applicant, incomes, new ArrayList<Link>(), "9600")

        when:
        FinancialCheckValues categoryAApplicant = IncomeValidator.validateCategoryAApplicant(incomeProvingResponse, getDate(2015, Calendar.MARCH, 23), getDate(2015, Calendar.SEPTEMBER, 23))

        then:
        categoryAApplicant.equals(FinancialCheckValues.PASSED)

    }

    def getApplicant() {
        Applicant applicant = new Applicant()
        applicant.title = "Mr"
        applicant.forename = "Duncan"
        applicant.surname = "Sinclair"
        applicant.nino = "AA123456A"
        applicant
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


    Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        return cal.getTime()
    }

}
