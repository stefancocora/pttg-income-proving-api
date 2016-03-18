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
import uk.gov.digital.ho.proving.income.acl.EarningsService;
import uk.gov.digital.ho.proving.income.acl.EarningsServiceFailedToMapDataToDomainClass;
import uk.gov.digital.ho.proving.income.acl.EarningsServiceNoUniqueMatch;
import uk.gov.digital.ho.proving.income.domain.Application;

import java.util.Date;
import java.util.regex.Pattern;

@RestController
public class Service {
    private Logger LOGGER = LoggerFactory.getLogger(Service.class);

    @Autowired
    private EarningsService earningsService;

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
            Date applicationDate = new Date();
            Application application = earningsService.lookup(nino, applicationDate);
            TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
            response.setApplication(application);
            return new ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse>(response, headers, HttpStatus.OK);
        } catch (EarningsServiceFailedToMapDataToDomainClass | EarningsServiceNoUniqueMatch e) {
            LOGGER.error("Could not retrieve earning details.");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            LOGGER.error("NINO is not valid");
            ValidationError error = new ValidationError("0001","NINO is invalid.");
            TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
            response.setError(error);
            return new ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse>(response, headers, HttpStatus.BAD_REQUEST);
        }
    }

    private String sanitiseNino(String nino) {
        return nino.replaceAll("\\s","");
    }

    private void validateNino(String nino) {
//        final Pattern pattern = Pattern.compile("/^[A-CEGHJ-PR-TW-Z]{1}[A-CEGHJ-NPR-TW-Z]{1}[0-9]{6}[A-D]{1}$/i");
        final Pattern pattern = Pattern.compile("^[a-zA-Z]{2}[0-9]{6}[a-zA-Z]{1}$");
        if (!pattern.matcher(nino).matches()) {
            throw new IllegalArgumentException("Invalid String");
        }
    }
}