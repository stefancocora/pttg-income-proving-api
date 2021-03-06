package uk.gov.digital.ho.proving.income.acl.test

import com.mongodb.DBCollection
import com.mongodb.DBCursor
import com.mongodb.DBObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import spock.lang.Ignore
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.ServiceConfiguration
import uk.gov.digital.ho.proving.income.acl.EarningsServiceFailedToMapDataToDomainClass
import uk.gov.digital.ho.proving.income.acl.EarningsServiceNoUniqueMatch
import uk.gov.digital.ho.proving.income.acl.MongodbBackedEarningsService
import uk.gov.digital.ho.proving.income.domain.Application

import java.time.LocalDate

@SpringApplicationConfiguration(ServiceConfiguration.class)
@Ignore //todo fix problem with @value injection on ServiceConfiguration.java
class MongodbBackedEarningsServiceSpec extends Specification {

    @Autowired
    MongodbBackedEarningsService sut;


    def "multiple records exist for applicant"() {

        sut.applicationsCollection = Stub(DBCollection) {
            find(_) >> {
                Stub(DBCursor) {
                    size() >> 2
                }
            }
        }

        when:

        Application result = sut.lookup("AA123456A", LocalDate.now())

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

        sut.lookup("AA123456A", LocalDate.now())

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

        sut.lookup("AA123456A", LocalDate.now())

        then:

        EarningsServiceFailedToMapDataToDomainClass ex = thrown()

    }
}
