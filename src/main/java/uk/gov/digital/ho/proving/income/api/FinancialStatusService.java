package uk.gov.digital.ho.proving.income.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.proving.income.acl.*;
import uk.gov.digital.ho.proving.income.domain.Application;
import uk.gov.digital.ho.proving.income.domain.IncomeProvingResponse;

import java.text.ParseException;
import java.util.Date;

@RestController
@ControllerAdvice
public class FinancialStatusService extends AbstractIncomeProvingController {

    @Autowired
    private EarningsService earningsService;

    @Autowired
    private IndividualService individualService;

    private static final int MINIMUM_DEPENDANTS = 0;
    private static final int MAXIMUM_DEPENDANTS = 99;

    private static final int NUMBER_OF_DAYS = 182;


    // TODO Some of these parameters should be mandatory
    @RequestMapping(value = "/incomeproving/v1/individual/{nino}/financialstatus", method = RequestMethod.GET)
    public ResponseEntity<FinancialStatusCheckResponse> getTemporaryMigrationFamilyApplication(
        @PathVariable(value = "nino") String nino,
        @RequestParam(value = "applicationRaisedDate") String applicationDateAsString,
        @RequestParam(value = "dependants", required = false) Integer dependants) {

        LOGGER.info(String.format("Income Proving Service API for Temporary Migration Family Application invoked for %s application received on %s.", nino, applicationDateAsString));

        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, APPLICATION_JSON);

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

            if (applicationDateAsString == null || applicationDateAsString.isEmpty()) {
                throw new IllegalArgumentException("applicationRaisedDate");
            }
            sdf.setLenient(false);
            Date applicationRaisedDate = sdf.parse(applicationDateAsString);
            Date startSearchDateDays = subtractXDays(applicationRaisedDate, NUMBER_OF_DAYS);
            IncomeProvingResponse incomeProvingResponse = individualService.lookup(nino, startSearchDateDays, applicationRaisedDate);

            Application application = earningsService.lookup(nino, applicationRaisedDate);

            FinancialStatusCheckResponse response = new FinancialStatusCheckResponse();
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
            return buildErrorResponse(headers, "0004", "Resource not found", HttpStatus.NOT_FOUND);
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

    protected ResponseEntity<FinancialStatusCheckResponse> buildErrorResponse(HttpHeaders headers, String statusCode, String statusMessage, HttpStatus status) {
        ResponseStatus error = new ResponseStatus(statusCode, statusMessage);
        FinancialStatusCheckResponse response = new FinancialStatusCheckResponse();
        response.setStatus(error);
        return new ResponseEntity<>(response, headers, status);
    }

}
