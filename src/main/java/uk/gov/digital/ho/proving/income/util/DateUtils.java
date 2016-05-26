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

    /**
     * Parse a string date representation in the ISO format (yyyy-mm-dd)
     * @param date
     * @return LocalDate corresponding to the input or empty if the input string does not parse
     */
    public static Optional<LocalDate> parseIsoDate(String date) {
        try {
            return Optional.of(LocalDate.parse(date, ISO_LOCAL_DATE));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Convert LocalDate to Date using the system default time zone.
     * @param localDate
     * @return
     */
    public static Date dateWithDefaultZone(LocalDate localDate){
        return Date.from(asLocalInstant(localDate));
    }


    private static Instant asLocalInstant(LocalDate localDate) {
        return localDate.atStartOfDay().atZone(systemDefault()).toInstant();
    }
}
