package uk.gov.digital.ho.proving.income.api.test

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.acl.EarningsService
import uk.gov.digital.ho.proving.income.acl.EarningsServiceNoUniqueMatch
import uk.gov.digital.ho.proving.income.api.Service
import uk.gov.digital.ho.proving.income.api.TemporaryMigrationFamilyCaseworkerApplicationResponse

class ServiceSpec extends Specification {

    Service sut = new Service();

    def "valid NINO is looked up on the earnings sevice"() {
        given:

        sut.earningsService = Stub(EarningsService)

        when:

        ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> result = sut.getTemporaryMigrationFamilyApplication("AA123456A", "")

        then:

        result.statusCode == HttpStatus.OK
    }

    def "invalid nino is rejected"() {

        when:

        ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> result = sut.getTemporaryMigrationFamilyApplication("AA123456AX", "")

        then:

        result.statusCode == HttpStatus.BAD_REQUEST

    }

    def "unknown nino yields HTTP Not Found (404)"() {
        given:

        EarningsService stubEarningsService = Stub()
        stubEarningsService.lookup(_,_) >> {
            throw new EarningsServiceNoUniqueMatch()
        }

        sut.earningsService = stubEarningsService

        when:

        ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> result = sut.getTemporaryMigrationFamilyApplication("AA123456X", "")

        then:

        result.statusCode == HttpStatus.NOT_FOUND

    }
}
