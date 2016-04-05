package uk.gov.digital.ho.proving.income.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.digital.ho.proving.income.domain.Income;
import uk.gov.digital.ho.proving.income.domain.IncomeProvingResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class IncomeValidator {

    private static final int NUMBER_OF_MONTHS = 6;
    private static final BigDecimal MONTHLY_THRESHOLD = new BigDecimal(1550);

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomeValidator.class);

    private IncomeValidator() {
    }

    static boolean isCategoryAApplicant(IncomeProvingResponse incomeProvingResponse, Date lower, Date upper) {
        return hasSameEmployerForLastXMonths(incomeProvingResponse.getIncomes(), NUMBER_OF_MONTHS, MONTHLY_THRESHOLD, lower, upper);
    }

    //TODO Refactor date handling once we know more about the back end and test env
    private static boolean hasSameEmployerForLastXMonths(List<Income> incomes, int numOfMonths, BigDecimal threshold, Date lower, Date upper) {
        Stream<Income> applicantIncome = incomes.stream()
                .sorted((income1, income2) -> income2.getPayDate().compareTo(income1.getPayDate()))
                .filter(income -> isDateInRange(income.getPayDate(), lower, upper));

        List<Income> lastXMonths = applicantIncome.limit(numOfMonths).collect(Collectors.toList());
        if (lastXMonths.size() >= numOfMonths) {

            // Do we have NUMBER_OF_MONTHS consecutive months with the same employer
            for (int i = 0; i < numOfMonths - 1; i++) {
                if (!isSuccessor(lastXMonths.get(i), lastXMonths.get(i + 1))) {
                    LOGGER.info("FAILED: Months not consecutive");
                    return false;
                }
            }
            // Check that each payment is passes the threshold check
            for (Income income : lastXMonths) {
                LOGGER.info("income value: " + new BigDecimal(income.getIncome()));
                if (threshold.compareTo(new BigDecimal(income.getIncome())) > 0) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private static boolean isSuccessor(Income first, Income second) {
        if (!first.getEmployer().toLowerCase().trim().equals(second.getEmployer().toLowerCase().trim())) {
            return false;
        } else {
            return getDifferenceInMonthsBetweenDates(first.getPayDate(), second.getPayDate()) == 1;
        }
    }

    private static long getDifferenceInMonthsBetweenDates(Date date1, Date date2) {
        LocalDate toDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fromDate = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long months = fromDate.until(toDate, ChronoUnit.MONTHS);
        LOGGER.info("fromDate: " + fromDate);
        LOGGER.info("toDate: " + toDate);

        LOGGER.info("Months: " + months);
        return months;
    }

    private static boolean isDateInRange(Date date, Date lower, Date upper) {
        boolean inRange =  !(date.before(lower) || date.after(upper));
        LOGGER.info(String.format("%s: %s in range of %s & %s", inRange, date, lower, upper ));
        return inRange;
    }

}
