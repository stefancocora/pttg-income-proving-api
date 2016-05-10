package uk.gov.digital.ho.proving.income.api.test

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.acl.IndividualService
import uk.gov.digital.ho.proving.income.acl.EarningsService
import uk.gov.digital.ho.proving.income.acl.EarningsServiceNoUniqueMatch
import uk.gov.digital.ho.proving.income.api.Service
import uk.gov.digital.ho.proving.income.api.TemporaryMigrationFamilyCaseworkerApplicationResponse
import uk.gov.digital.ho.proving.income.domain.Individual
import uk.gov.digital.ho.proving.income.domain.Income
import uk.gov.digital.ho.proving.income.domain.IncomeProvingResponse

class ServiceSpec extends Specification {

    final String PIZZA_HUT = "Pizza Hut"
    final String BURGER_KING = "Burger King"

    Service sut = new Service();

    def "valid NINO is looked up on the earnings service"() {
        given:

        sut.earningsService = Stub(EarningsService)

        IndividualService stubIndividualService = Stub()
        Individual individual = getIndividual()
        List<Income> incomes = getConsecutiveIncomes()
        stubIndividualService.lookup(_,_,_) >> {
           new IncomeProvingResponse(individual, incomes, "9600", "M1")
        }
        sut.individualService = stubIndividualService

        when:

        ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> result = sut.getTemporaryMigrationFamilyApplication("AA123456A", "2015-09-23", 0)

        then:

        result.statusCode == HttpStatus.OK
    }

    def "invalid nino is rejected"() {

        when:

        ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> result = sut.getTemporaryMigrationFamilyApplication("AA123456AX", "2016-03-21", 0)

        then:

        result.statusCode == HttpStatus.BAD_REQUEST

    }

    def "unknown nino yields HTTP Not Found (404)"() {
        given:
        IndividualService stubIndividualService = Stub()
        Individual individual = getIndividual()
        List<Income> incomes = getConsecutiveIncomes()
        stubIndividualService.lookup(_,_,_) >> {
           new IncomeProvingResponse(individual, incomes, "9600", "M1")
        }
        sut.individualService = stubIndividualService

        EarningsService stubEarningsService = Stub()
        stubEarningsService.lookup(_, _) >> {
            throw new EarningsServiceNoUniqueMatch()
        }
        sut.earningsService = stubEarningsService

        when:
        ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> result = sut.getTemporaryMigrationFamilyApplication("AA123456C", "2016-03-21", 0)
        then:
        result.statusCode == HttpStatus.NOT_FOUND

    }

    def "cannot submit less than zero dependants"() {
        when:
        ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> result = sut.getTemporaryMigrationFamilyApplication("AA123456C", "2016-03-21", -1)
        then:
        result.statusCode == HttpStatus.BAD_REQUEST
    }

    def "can submit more than zero dependants"() {
        given:
        sut.earningsService = Stub(EarningsService)

        IndividualService stubIndividualService = Stub()
        Individual individual = getIndividual()
        List<Income> incomes = getConsecutiveIncomes()
        stubIndividualService.lookup(_,_,_) >> {
            new IncomeProvingResponse(individual, incomes, "9600", "M1")
        }
        sut.individualService = stubIndividualService
        when:
        ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> result = sut.getTemporaryMigrationFamilyApplication("AA123456A", "2015-09-23", 1)
        then:
        result.statusCode == HttpStatus.OK
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
        incomes.add(new Income(getDate(2015, Calendar.JANUARY, 15),PIZZA_HUT , "1400" ))
        incomes.add(new Income(getDate(2015, Calendar.MAY, 15),PIZZA_HUT , "1600" ))
        incomes.add(new Income(getDate(2015, Calendar.JUNE, 15),PIZZA_HUT , "1600" ))
        incomes.add(new Income(getDate(2015, Calendar.APRIL, 15),PIZZA_HUT , "1600" ))
        incomes.add(new Income(getDate(2015, Calendar.JULY, 15),PIZZA_HUT , "1600" ))
        incomes.add(new Income(getDate(2015, Calendar.FEBRUARY, 15),BURGER_KING , "1600" ))
        incomes.add(new Income(getDate(2015, Calendar.AUGUST, 15),PIZZA_HUT , "1600" ))
        incomes.add(new Income(getDate(2015, Calendar.SEPTEMBER, 15),PIZZA_HUT , "1600" ))
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
