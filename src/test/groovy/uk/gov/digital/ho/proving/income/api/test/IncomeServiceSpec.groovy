package uk.gov.digital.ho.proving.income.api.test

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.api.FinancialStatusCheckResponse
import uk.gov.digital.ho.proving.income.api.IncomeRetrievalResponse
import uk.gov.digital.ho.proving.income.api.IncomeRetrievalService

import static java.time.LocalDate.now
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

/**
 * @Author Home Office Digital
 */
class IncomeServiceSpec extends Specification {

    IncomeRetrievalService sut = new IncomeRetrievalService();

    String tomorrow = now().plusDays(1).format(ISO_LOCAL_DATE);
    String yesterday = now().minusDays(1).format(ISO_LOCAL_DATE);

    def "invalid from date is rejected"() {
        when:
        ResponseEntity<IncomeRetrievalResponse> result = sut.getIncome("AA123456A", "2016-03-xx", yesterday)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
    }

    def "invalid to date is rejected"() {
        when:
        ResponseEntity<IncomeRetrievalResponse> result = sut.getIncome("AA123456A", yesterday, "2016-03-xx")

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
    }

    def "invalid from and to dates are rejected"() {
        when:
        ResponseEntity<IncomeRetrievalResponse> result = sut.getIncome("AA123456A", "2016-03-xx", "2016-03-xx")

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
    }

    def "future from date is rejected"() {
        when:
        ResponseEntity<IncomeRetrievalResponse> result = sut.getIncome("AA123456A", tomorrow, yesterday)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
    }

    def "future to date is rejected"() {
        when:
        ResponseEntity<IncomeRetrievalResponse> result = sut.getIncome("AA123456A", yesterday, tomorrow)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
    }

    def "future from and to dates are rejected"() {
        when:
        ResponseEntity<IncomeRetrievalResponse> result = sut.getIncome("AA123456A", tomorrow, tomorrow)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
    }
}
