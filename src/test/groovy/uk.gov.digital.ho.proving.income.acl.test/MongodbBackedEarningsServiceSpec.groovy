package uk.gov.digital.ho.proving.income.acl.test

import com.mongodb.DBCollection
import com.mongodb.DBCursor
import com.mongodb.DBObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.ServiceConfiguration
import uk.gov.digital.ho.proving.income.acl.EarningsServiceFailedToMapDataToDomainClass
import uk.gov.digital.ho.proving.income.acl.EarningsServiceNoUniqueMatch
import uk.gov.digital.ho.proving.income.acl.MongodbBackedEarningsService
import uk.gov.digital.ho.proving.income.domain.Application

@SpringApplicationConfiguration(ServiceConfiguration.class)
class MongodbBackedEarningsServiceSpec extends Specification {

    @Autowired
    MongodbBackedEarningsService sut;

    def "earnings records are available"() {

        when:

        Application result = sut.lookup("AA123456A", new Date())

        then:

        result.individual.nino
        result.individual.title
        result.individual.forename
        result.individual.surname

    }

    def "multiple records exist for applicant"() {

        sut.applicationsCollection = Stub(DBCollection) {
            find(_) >> {
                Stub(DBCursor) {
                    size() >> 2
                }
            }
        }

        when:

        Application result = sut.lookup("AA123456A", new Date())

        then:

        EarningsServiceNoUniqueMatch ex = thrown()
    }

    def "invalid json"() {
        sut.applicationsCollection = Stub(DBCollection) {
            find(_) >> {
                Stub(DBCursor) {
                    size() >> 1
                    next() >> Mock(DBObject)
                }
            }
        }

        when:

        sut.lookup("AA123456A", new Date())

        then:

        EarningsServiceFailedToMapDataToDomainClass ex = thrown()

    }

    def "json that does not contain an Application"() {

        DBObject stubDBObject = Stub()
        stubDBObject.toString() >> '{ "_id":"the id", "fred":1 }'

        DBCursor stubDBCursor = Stub()
        stubDBCursor.size() >> 1
        stubDBCursor.next() >> stubDBObject

        sut.applicationsCollection = Stub(DBCollection) {
            find(_) >> stubDBCursor
        }

        when:

        sut.lookup("AA123456A", new Date())

        then:

        EarningsServiceFailedToMapDataToDomainClass ex = thrown()

    }
}
