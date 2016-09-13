package uk.gov.digital.ho.proving.income

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.LoggingEvent
import ch.qos.logback.core.Appender
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.audit.AuditEventRepository
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.TestRestTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

import static java.time.LocalDateTime.now
import static java.time.LocalDateTime.parse
import static java.time.temporal.ChronoUnit.MINUTES

/**
 * @Author Home Office Digital
 */
@SpringApplicationConfiguration(classes = ServiceRunner.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@TestPropertySource(properties = [
    "mongodb.service=unknownhost",  // Don't rely on pre-loaded data in a real mongo
    "mongodb.connect.timeout.millis=100", // Don't hang around trying to connect to a mongo that isn't there
    "apidocs.dir=static"
])
class AuditIntegrationSpec extends Specification {

    @Value('${local.server.port}')
    def port

    def path = "/incomeproving/v1/individual/AA123456A/income?"
    def params = "fromDate=2014-12-01&toDate=2015-01-01"
    def url

    RestTemplate restTemplate

    @Autowired
    AuditEventRepository auditEventRepository

    Appender logAppender = Mock()

    def setup() {
        restTemplate = new TestRestTemplate()
        url = "http://localhost:" + port + path + params

        withMockLogAppender()
    }

    def withMockLogAppender() {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.addAppender(logAppender);
    }

    def "Searches are audited as INFO level log output with AUDIT prefix and SEARCH type with a timestamp"() {

        given:

        List<LoggingEvent> logEntries = []

        _ * logAppender.doAppend(_) >> { arg ->

            if (arg[0].formattedMessage.contains("AUDIT")) {
                logEntries.add(arg[0])
            }
        }

        when:
        restTemplate.getForEntity(url, String.class)
        LoggingEvent logEntry = logEntries[0]

        then:

        logEntry.level == Level.INFO

        // We can capture the SEARCH event log even though the search fails because there is no mongo

        logEntry.formattedMessage.contains("principal=anonymous")
        logEntry.formattedMessage.contains("type=SEARCH")
        logEntry.formattedMessage.contains("method=get-income")

        LocalDateTime timestamp =
            Instant.ofEpochMilli(logEntry.timeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime();

        MINUTES.between(timestamp, now()) < 1;
    }


}
