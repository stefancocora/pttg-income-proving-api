package uk.gov.digital.ho.proving.income.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
/**
 * @Author Home Office Digital
 */
public class DateUtils {

    public static Instant asLocalInstant(LocalDate localDate) {
        return localDate.atStartOfDay().atZone(systemDefault()).toInstant();
    }

    public static Optional<Date> parseDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date, ISO_LOCAL_DATE);
            return Optional.of(Date.from(asLocalInstant(localDate)));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

}
