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
import uk.gov.digital.ho.proving.income.acl.ApplicantService;
import uk.gov.digital.ho.proving.income.acl.EarningsService;
import uk.gov.digital.ho.proving.income.acl.EarningsServiceFailedToMapDataToDomainClass;
import uk.gov.digital.ho.proving.income.acl.EarningsServiceNoUniqueMatch;
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
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping(value="/application", method= RequestMethod.GET)
    public ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> getTemporaryMigrationFamilyApplication(
            @RequestParam(value="nino", required=false) String nino,
            @RequestParam(value="applicationReceivedDate", required=false) String applicationDateAsString) {
        LOGGER.info(String.format("Income Proving Service API for Temporary Migration Family Application invoked for %s application received on %s.", nino, applicationDateAsString));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type","application/json");

        try {
            nino = sanitiseNino(nino);
            validateNino(nino);
            // validate applicationDate
            sdf.setLenient(false);
            Date applicationDate = sdf.parse(applicationDateAsString);
            Date startSearchDate = subtractXMonths(applicationDate, NUMBER_OF_MONTHS);
            IncomeProvingResponse incomeProvingResponse = applicantService.lookup(nino, startSearchDate, applicationDate);

            FinancialCheckValues categoryAApplicant = IncomeValidator.validateCategoryAApplicant(incomeProvingResponse, startSearchDate, applicationDate);

            Application application = earningsService.lookup(nino, applicationDate);

            if (categoryAApplicant.equals(FinancialCheckValues.PASSED)) {
                application.getFinancialRequirementsCheck().setMet(true);
                application.getFinancialRequirementsCheck().setFailureReason(null);
            } else {
                application.getFinancialRequirementsCheck().setMet(false);
                application.getFinancialRequirementsCheck().setFailureReason(categoryAApplicant.toString());
            }
            TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
            response.setApplication(application);
            return new ResponseEntity<>(response, headers, HttpStatus.OK);

        } catch (EarningsServiceFailedToMapDataToDomainClass | EarningsServiceNoUniqueMatch e) {
            LOGGER.error("Could not retrieve earning details.");
            ValidationError error = new ValidationError("0003","Could not retrieve earning details.");
            TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
            response.setError(error);
            headers.clear();
            return new ResponseEntity(response, headers, HttpStatus.NOT_FOUND);
        } catch (ParseException e) {
            LOGGER.error("Error parsing date", e);
            ValidationError error = new ValidationError("0002","Application Date is invalid.");
            TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
            response.setError(error);
            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            LOGGER.error("NINO is not valid", e);
            ValidationError error = new ValidationError("0001","NINO is invalid.");
            TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
            response.setError(error);
            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        }
    }

    private Date subtractXMonths(Date date, int months) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.minusMonths(months).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private String sanitiseNino(String nino) {
        return nino.replaceAll("\\s","").toUpperCase();
    }

    private void validateNino(String nino) {
//        final Pattern pattern = Pattern.compile("/^[A-CEGHJ-PR-TW-Z]{1}[A-CEGHJ-NPR-TW-Z]{1}[0-9]{6}[A-D]{1}$/i");
        final Pattern pattern = Pattern.compile("^[a-zA-Z]{2}[0-9]{6}[a-dA-D]{1}$");
        if (!pattern.matcher(nino).matches()) {
            throw new IllegalArgumentException("Invalid String");
        }
    }
}