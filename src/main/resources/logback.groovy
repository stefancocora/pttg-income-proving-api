import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import ch.qos.logback.core.status.OnConsoleStatusListener
import net.logstash.logback.composite.loggingevent.ArgumentsJsonProvider
import net.logstash.logback.composite.loggingevent.LoggingEventFormattedTimestampJsonProvider
import net.logstash.logback.composite.loggingevent.LoggingEventJsonProviders
import net.logstash.logback.composite.loggingevent.LoggingEventPatternJsonProvider
import net.logstash.logback.composite.loggingevent.LogstashMarkersJsonProvider
import net.logstash.logback.composite.loggingevent.MdcJsonProvider
import net.logstash.logback.composite.loggingevent.MessageJsonProvider
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder

import static ch.qos.logback.classic.Level.*

def appName = "pttg-income-proving-api"
def version = "0.1.RELEASE"

// Add a status listener to record the state of the logback configuration when the logging system is initialised.
statusListener(OnConsoleStatusListener)

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}

appender("FILE", FileAppender) {
    file = "income-proving-api.log"
    append = true

    encoder(LoggingEventCompositeJsonEncoder) {
        providers(LoggingEventJsonProviders) {
            pattern(LoggingEventPatternJsonProvider) {
                pattern = """{ "appName": "${appName}", "appVersion":"${version}", "level": "%-5level", "thread": "%thread", "logger": "%logger{36}" }"""
            }
            message(MessageJsonProvider)
            mdc(MdcJsonProvider)
            arguments(ArgumentsJsonProvider)
            logstashMarkers(LogstashMarkersJsonProvider)
            timestamp(LoggingEventFormattedTimestampJsonProvider)
        }
    }

    filter(ch.qos.logback.classic.filter.ThresholdFilter) {
        level = DEBUG
    }
}

// Define logging levels for specific packages
logger("org.eclipse.jetty", INFO)
logger("org.mongodb.driver.cluster", INFO)
logger("org.springframework", INFO)
logger("org.mongodb.driver.connection", INFO)

root(DEBUG, ["STDOUT","FILE"])

// Check config file every 30 seconds and reload if changed
scan("30 seconds")
