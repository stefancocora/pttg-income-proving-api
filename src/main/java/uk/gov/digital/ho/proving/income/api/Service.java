package uk.gov.digital.ho.proving.income.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import uk.gov.digital.ho.proving.income.acl.*;
import uk.gov.digital.ho.proving.income.domain.Application;
import uk.gov.digital.ho.proving.income.domain.IncomeProvingResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

@RestController
@ControllerAdvice
public class Service {
    private Logger LOGGER = LoggerFactory.getLogger(Service.class);

    @Autowired
    private EarningsService earningsService;

    @Autowired
    private IndividualService individualService;

    private static final int MINIMUM_DEPENDANTS = 0;
    private static final int MAXIMUM_DEPENDANTS = 99;

    private static final int NUMBER_OF_DAYS = 182;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // TODO Some of these parameters should be mandatory
    @RequestMapping(value = "/incomeproving/v1/individual/{nino}/financialstatus", method = RequestMethod.GET)
    public ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> getTemporaryMigrationFamilyApplication(
        @PathVariable(value = "nino") String nino,
        @RequestParam(value = "applicationRaisedDate") String applicationDateAsString,
        @RequestParam(value = "dependants", required = false) Integer dependants) {

        LOGGER.info(String.format("Income Proving Service API for Temporary Migration Family Application invoked for %s application received on %s.", nino, applicationDateAsString));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/json");

        try {
            nino = sanitiseNino(nino);
            validateNino(nino);

            if (dependants == null) {
                dependants = 0;
            } else if (dependants < MINIMUM_DEPENDANTS) {
                throw new IllegalArgumentException("Dependants cannot be less than " + MINIMUM_DEPENDANTS);
            } else if (dependants > MAXIMUM_DEPENDANTS) {
                throw new IllegalArgumentException("Dependants cannot be more than " + MAXIMUM_DEPENDANTS);
            }

            sdf.setLenient(false);
            Date applicationRaisedDate = sdf.parse(applicationDateAsString);
            Date startSearchDateDays = subtractXDays(applicationRaisedDate, NUMBER_OF_DAYS);
            IncomeProvingResponse incomeProvingResponse = individualService.lookup(nino, startSearchDateDays, applicationRaisedDate);

            Application application = earningsService.lookup(nino, applicationRaisedDate);

            TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
            response.setIndividual(application.getIndividual());

            switch (incomeProvingResponse.getPayFreq().toUpperCase()) {
                case "M1":
                    FinancialCheckValues categoryAMonthlySalaried = IncomeValidator.validateCategoryAMonthlySalaried(incomeProvingResponse.getIncomes(), startSearchDateDays, applicationRaisedDate, dependants);
                    if (categoryAMonthlySalaried.equals(FinancialCheckValues.MONTHLY_SALARIED_PASSED)) {
                        response.setCategoryCheck(new CategoryCheck("A",true, null, applicationRaisedDate, startSearchDateDays));
                    } else {
                        response.setCategoryCheck(new CategoryCheck("A",false, categoryAMonthlySalaried, applicationRaisedDate, startSearchDateDays));
                    }
                    break;
                case "W1":
                    FinancialCheckValues categoryAWeeklySalaried = IncomeValidator.validateCategoryAWeeklySalaried(incomeProvingResponse.getIncomes(), startSearchDateDays, applicationRaisedDate, dependants);
                    if (categoryAWeeklySalaried.equals(FinancialCheckValues.WEEKLY_SALARIED_PASSED)) {
                        response.setCategoryCheck(new CategoryCheck("A",true, null, applicationRaisedDate, startSearchDateDays));
                    } else {
                        response.setCategoryCheck(new CategoryCheck("A",false, categoryAWeeklySalaried, applicationRaisedDate, startSearchDateDays));
                    }
                    break;
                default:
                    throw new UnknownPaymentFrequencyType();
            }
            response.setStatus(new ResponseStatus("100", "OK"));
            return new ResponseEntity<>(response, headers, HttpStatus.OK);

        } // TODO All this below is a mess of exceptions and needs to be refactored
        catch (EarningsServiceFailedToMapDataToDomainClass | EarningsServiceNoUniqueMatch e) {
            LOGGER.error("Could not retrieve earning details.", e);
            return buildErrorResponse(headers, "0003", "Could not retrieve earning details", HttpStatus.NOT_FOUND);
        } catch (ParseException e) {
            LOGGER.error("Error parsing date", e);
            return buildErrorResponse(headers, "0002", "Parameter error: Application raised date is invalid", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException iae) {
            LOGGER.error(iae.getMessage(), iae);
            return buildErrorResponse(headers, "0004", "Parameter error: " + iae.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnknownPaymentFrequencyType upte) {
            LOGGER.error("Unknown payment frequency type " + upte);
            return buildErrorResponse(headers, "0004", "Unknown payment frequency type", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            LOGGER.error("NINO is not valid", e);
            return buildErrorResponse(headers, "0001", "Parameter error: NINO is invalid", HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> buildErrorResponse(HttpHeaders headers, String statusCode, String statusMessage, HttpStatus status) {
        ResponseStatus error = new ResponseStatus(statusCode, statusMessage);
        TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
        response.setStatus(error);
        return new ResponseEntity<>(response, headers, status);
    }


    private Date subtractXDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

    private String sanitiseNino(String nino) {
        return nino.replaceAll("\\s", "").toUpperCase();
    }

    private void validateNino(String nino) {
        final Pattern pattern = Pattern.compile("^[a-zA-Z]{2}[0-9]{6}[a-dA-D]{1}$");
        if (!pattern.matcher(nino).matches()) {
            throw new IllegalArgumentException("Parameter error: Invalid NINO");
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object missingParamterHandler(MissingServletRequestParameterException exception) {
        LOGGER.debug(exception.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/json");
        return buildErrorResponse(headers, "0008", "Missing parameter: " + exception.getParameterName() , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Object requestHandlingNoHandlerFound(NoHandlerFoundException exception) {
        LOGGER.debug(exception.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/json");
        return buildErrorResponse(headers, "0008", "Resource not found: " + exception.getRequestURL() , HttpStatus.NOT_FOUND);
    }
}
