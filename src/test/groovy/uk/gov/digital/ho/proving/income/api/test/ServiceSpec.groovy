package uk.gov.digital.ho.proving.income.api.test

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.acl.ApplicantService
import uk.gov.digital.ho.proving.income.acl.EarningsService
import uk.gov.digital.ho.proving.income.acl.EarningsServiceNoUniqueMatch
import uk.gov.digital.ho.proving.income.api.Service
import uk.gov.digital.ho.proving.income.api.TemporaryMigrationFamilyCaseworkerApplicationResponse
import uk.gov.digital.ho.proving.income.domain.Applicant
import uk.gov.digital.ho.proving.income.domain.Income
import uk.gov.digital.ho.proving.income.domain.IncomeProvingResponse
import uk.gov.digital.ho.proving.income.domain.Link

class ServiceSpec extends Specification {

    final String PIZZA_HUT = "Pizza Hut"
    final String BURGER_KING = "Burger King"

    Service sut = new Service();

    def "valid NINO is looked up on the earnings service"() {
        given:

        sut.earningsService = Stub(EarningsService)

        ApplicantService stubApplicantService = Stub()
        Applicant applicant = getApplicant()
        List<Income> incomes = getConsecutiveIncomes()
        stubApplicantService.lookup(_,_,_) >> {
           new IncomeProvingResponse(applicant, incomes, new ArrayList<Link>(), "9600")
        }
        sut.applicantService = stubApplicantService

        when:

        ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> result = sut.getTemporaryMigrationFamilyApplication("AA123456A", "2015-09-23")

        then:

        result.statusCode == HttpStatus.OK
    }

    def "invalid nino is rejected"() {

        when:

        ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> result = sut.getTemporaryMigrationFamilyApplication("AA123456AX", "2016-03-21")

        then:

        result.statusCode == HttpStatus.BAD_REQUEST

    }

    def "unknown nino yields HTTP Not Found (404)"() {
        given:

        ApplicantService stubApplicantService = Stub()
        Applicant applicant = getApplicant()
        List<Income> incomes = getConsecutiveIncomes()
        stubApplicantService.lookup(_,_,_) >> {
           new IncomeProvingResponse(applicant, incomes, new ArrayList<Link>(), "9600")
        }
        sut.applicantService = stubApplicantService

        EarningsService stubEarningsService = Stub()
        stubEarningsService.lookup(_, _) >> {
            throw new EarningsServiceNoUniqueMatch()
        }
        sut.earningsService = stubEarningsService

        when:

        ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> result = sut.getTemporaryMigrationFamilyApplication("AA123456X", "2016-03-21")

        then:

        result.statusCode == HttpStatus.NOT_FOUND

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
