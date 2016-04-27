package uk.gov.digital.ho.proving.income.api;

import java.math.BigDecimal;

public class MonthlyThresholdCalculator {

    private BigDecimal dependants = BigDecimal.ZERO;
    private BigDecimal subsequentDependants = BigDecimal.ZERO;

    private final BigDecimal BASE_THRESHOLD_VALUE = new BigDecimal(18600);
    private final BigDecimal FIRST_DEPENDANT_VALUE = new BigDecimal(3800);
    private final BigDecimal SUBSEQUENT_DEPENDANT_VALUE = new BigDecimal(2400);
    private final BigDecimal MONTHS = new BigDecimal(12);

    private BigDecimal threshold = BigDecimal.ZERO;

    public MonthlyThresholdCalculator(int dependants) throws IllegalArgumentException {

        if (dependants < 0) {
            throw new IllegalArgumentException("Number of dependants cannot be less than zero.");
        } else {
            this.dependants = new BigDecimal(dependants);
            this.subsequentDependants = new BigDecimal(dependants - 1);
            threshold = calcThreshold();
        }
    }

    private BigDecimal calcThreshold() {
        BigDecimal thresholdValue;
        if (dependants.compareTo(BigDecimal.ZERO) == 0) {
            thresholdValue = BASE_THRESHOLD_VALUE;
        } else {
            thresholdValue = (BASE_THRESHOLD_VALUE.add(FIRST_DEPENDANT_VALUE).add(SUBSEQUENT_DEPENDANT_VALUE.multiply(subsequentDependants)));
        }
        return thresholdValue.divide(MONTHS, 2, BigDecimal.ROUND_CEILING);
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

}
