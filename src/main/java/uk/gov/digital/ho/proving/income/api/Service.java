package uk.gov.digital.ho.proving.income.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.proving.income.acl.*;
import uk.gov.digital.ho.proving.income.domain.Application;
import uk.gov.digital.ho.proving.income.domain.IncomeProvingResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

@RestController
public class Service {
    private Logger LOGGER = LoggerFactory.getLogger(Service.class);

    @Autowired
    private EarningsService earningsService;

    @Autowired
    private ApplicantService applicantService;

    private static final int NUMBER_OF_MONTHS = 6;
    private static final int NUMBER_OF_DAYS = 182;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
    private final String END_OF_DAY = " 235959";

    // TODO Some of these parameters should be mandatory
    @RequestMapping(value = "/application", method = RequestMethod.GET)
    public ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> getTemporaryMigrationFamilyApplication(
            @RequestParam(value = "nino", required = false) String nino,
            @RequestParam(value = "applicationRaisedDate", required = false) String applicationDateAsString,
            @RequestParam(value = "dependants", required = false) Integer dependants) {

        LOGGER.info(String.format("Income Proving Service API for Temporary Migration Family Application invoked for %s application received on %s.", nino, applicationDateAsString));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/json");

        try {
            nino = sanitiseNino(nino);
            validateNino(nino);

            if (dependants == null) {
                dependants = 0;
            } else if (dependants < 0) {
                throw new IllegalArgumentException("Dependants cannot be less than zero");
            }

            sdf.setLenient(false);
            Date applicationDate = sdf.parse(applicationDateAsString + END_OF_DAY);
            Date startSearchDateDays = subtractXDays(applicationDate, NUMBER_OF_DAYS);
            Date startSearchDateMonths = subtractXMonths(applicationDate, NUMBER_OF_MONTHS);
            IncomeProvingResponse incomeProvingResponse = applicantService.lookup(nino, startSearchDateDays, applicationDate);

            Application application = earningsService.lookup(nino, applicationDate);

            switch (incomeProvingResponse.getPayFreq().toUpperCase()) {
                case "M1":
                    FinancialCheckValues categoryAMonthlySalaried = IncomeValidator.validateCategoryAMonthlySalaried(incomeProvingResponse.getIncomes(), startSearchDateMonths, applicationDate, dependants);
                    if (categoryAMonthlySalaried.equals(FinancialCheckValues.MONTHLY_SALARIED_PASSED)) {
                        application.getFinancialRequirementsCheck().setMet(true);
                        application.getFinancialRequirementsCheck().setFailureReason(null);
                    } else {
                        application.getFinancialRequirementsCheck().setMet(false);
                        application.getFinancialRequirementsCheck().setFailureReason(categoryAMonthlySalaried.toString());
                    }
                    break;
                case "W1":
                    FinancialCheckValues categoryAWeeklySalaried = IncomeValidator.validateCategoryAWeeklySalaried(incomeProvingResponse.getIncomes(), startSearchDateDays, applicationDate, dependants);
                    if (categoryAWeeklySalaried.equals(FinancialCheckValues.WEEKLY_SALARIED_PASSED)) {
                        application.getFinancialRequirementsCheck().setMet(true);
                        application.getFinancialRequirementsCheck().setFailureReason(null);
                    } else {
                        application.getFinancialRequirementsCheck().setMet(false);
                        application.getFinancialRequirementsCheck().setFailureReason(categoryAWeeklySalaried.toString());
                    }
                    break;
                default:
                    throw new UnknownPaymentFrequencyType();
            }

            TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
            response.setApplication(application);
            return new ResponseEntity<>(response, headers, HttpStatus.OK);

        } // TODO All this below is a mess of exceptions and needs to be refactored
        catch (EarningsServiceFailedToMapDataToDomainClass | EarningsServiceNoUniqueMatch e) {
            LOGGER.error("Could not retrieve earning details.", e);
            return buildErrorResponse(headers, "0003", "Could not retrieve earning details", HttpStatus.NOT_FOUND);
        } catch (ParseException e) {
            LOGGER.error("Error parsing date", e);
            return buildErrorResponse(headers, "0002", "Application Date is invalid.", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException iae) {
            LOGGER.error(iae.getMessage(), iae);
            return buildErrorResponse(headers, "0004", iae.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnknownPaymentFrequencyType upte) {
            LOGGER.error("Unknown payment frequency type " + upte);
            return buildErrorResponse(headers, "0004", "Unknown payment frequency type", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            LOGGER.error("NINO is not valid", e);
            return buildErrorResponse(headers, "0001", "NINO is invalid.", HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> buildErrorResponse(HttpHeaders headers, String errorCode, String errorMessage, HttpStatus status) {
        ValidationError error = new ValidationError(errorCode, errorMessage);
        TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
        response.setError(error);
        return new ResponseEntity<>(response, headers, status);
    }

    private Date subtractXMonths(Date date, int months) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.minusMonths(months).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date subtractXDays(Date date, int days) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.minusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private String sanitiseNino(String nino) {
        return nino.replaceAll("\\s", "").toUpperCase();
    }

    private void validateNino(String nino) {
        final Pattern pattern = Pattern.compile("^[a-zA-Z]{2}[0-9]{6}[a-dA-D]{1}$");
        if (!pattern.matcher(nino).matches()) {
            throw new IllegalArgumentException("Invalid String");
        }
    }
}