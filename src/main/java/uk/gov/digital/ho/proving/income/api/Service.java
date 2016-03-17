package uk.gov.digital.ho.proving.income.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import uk.gov.digital.ho.proving.income.domain.Applicant;
import uk.gov.digital.ho.proving.income.domain.Application;
import uk.gov.digital.ho.proving.income.domain.TemporaryMigrationFamilyApplication;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Pattern;

@RestController
public class Service {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EarningsService revenueService;

    @RequestMapping(value="/application", method= RequestMethod.GET)
    public ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse> getTemporaryMigrationFamilyApplication(
            @RequestParam(value="nino", required=false) String nino,
            @RequestParam(value="applicationReceivedDate", required=false) String applicationDate) {
        Logger logger = LoggerFactory.getLogger(Service.class);
        logger.info(String.format("Income Proving Service API for Temporary Migration Family Application invoked for %s application received on %s.", nino, applicationDate));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type","application/json");

        try {
            nino = sanitiseNino(nino);
            validateNino(nino);
        } catch (RuntimeException e) {
            logger.error("NINO is not valid");
            ValidationError error = new ValidationError("0001","NINO is invalid.");
            TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
            response.setError(error);
            return new ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse>(response, headers, HttpStatus.BAD_REQUEST);
        }


        if (nino.equalsIgnoreCase("XXX")) {
            TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
            response.setApplication(buildApplication(nino));
            return new ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse>(response, HttpStatus.OK);
        }

        try {
            String responseString = revenueService.lookup(nino);

            Application application = mapper.readValue(responseString, TemporaryMigrationFamilyApplication.class);
            TemporaryMigrationFamilyCaseworkerApplicationResponse response = new TemporaryMigrationFamilyCaseworkerApplicationResponse();
            response.setApplication(application);

            return new ResponseEntity<TemporaryMigrationFamilyCaseworkerApplicationResponse>(response, headers, HttpStatus.OK);
        } catch (JsonMappingException | JsonParseException e) {
            logger.error("Error building response.", e);
        } catch (IOException e) {
            logger.error("Error building response.", e);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
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

    private Application buildApplication(String nino) {
        Applicant applicant = new Applicant("Mr", "Brian", "Snail", nino);

        TemporaryMigrationFamilyApplication application = new TemporaryMigrationFamilyApplication(applicant, new Date(), "A", true, new BigDecimal(18600.00));
        return application;
    }

}