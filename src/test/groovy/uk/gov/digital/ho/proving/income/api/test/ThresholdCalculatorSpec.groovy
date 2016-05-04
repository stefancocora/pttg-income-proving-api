package uk.gov.digital.ho.proving.income.api.test

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification
import uk.gov.digital.ho.proving.income.api.SalariedThresholdCalculator

class ThresholdCalculatorSpec extends Specification {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThresholdCalculatorSpec.class);

    def "throw error for negative number of dependants"() {
        given:
        SalariedThresholdCalculator calculator
        when:
        calculator = new SalariedThresholdCalculator(-1);
        BigDecimal threshold = calculator.getMonthlyThreshold()
        then:
        thrown IllegalArgumentException
    }

    def "calculate thresholds for zero dependant"() {
        given:
        SalariedThresholdCalculator calculator = new SalariedThresholdCalculator(0);
        when:
        BigDecimal threshold = calculator.getMonthlyThreshold()
        then:
        threshold.compareTo(BigDecimal.valueOf(1550.00)) == 0
    }

    def "calculate thresholds for one dependant"() {
        given:
        SalariedThresholdCalculator calculator = new SalariedThresholdCalculator(1);
        when:
        BigDecimal threshold = calculator.getMonthlyThreshold()
        then:
        threshold.compareTo(BigDecimal.valueOf(1866.67)) == 0
    }

    def "calculate thresholds for two dependant"() {
        given:
        SalariedThresholdCalculator calculator = new SalariedThresholdCalculator(2);
        when:
        BigDecimal threshold = calculator.getMonthlyThreshold()
        then:
        threshold.compareTo(BigDecimal.valueOf(2066.67)) == 0
    }

    def "calculate thresholds for three dependant"() {
        given:
        SalariedThresholdCalculator calculator = new SalariedThresholdCalculator(3)
        when:
        BigDecimal threshold = calculator.getMonthlyThreshold()
        then:
        threshold.compareTo(BigDecimal.valueOf(2266.67)) == 0
    }

    def "calculate thresholds for four dependant"() {
        given:
        SalariedThresholdCalculator calculator = new SalariedThresholdCalculator(4)
        when:
        BigDecimal threshold = calculator.getMonthlyThreshold()
        then:
        threshold.compareTo(BigDecimal.valueOf(2466.67)) == 0
    }

    def "calculate thresholds for five dependant"() {
        given:
        SalariedThresholdCalculator calculator = new SalariedThresholdCalculator(5)
        when:
        BigDecimal threshold = calculator.getMonthlyThreshold()
        then:
        threshold.compareTo(BigDecimal.valueOf(2666.67)) == 0
    }

    def "calculate thresholds for six dependant"() {
        given:
        SalariedThresholdCalculator calculator = new SalariedThresholdCalculator(6)
        when:
        BigDecimal threshold = calculator.getMonthlyThreshold()
        then:
        threshold.compareTo(BigDecimal.valueOf(2866.67)) == 0
    }

    def "calculate thresholds for seven dependant"() {
        given:
        SalariedThresholdCalculator calculator = new SalariedThresholdCalculator(7)
        when:
        BigDecimal threshold = calculator.getMonthlyThreshold()
        then:
        threshold.compareTo(BigDecimal.valueOf(3066.67)) == 0
    }

}
