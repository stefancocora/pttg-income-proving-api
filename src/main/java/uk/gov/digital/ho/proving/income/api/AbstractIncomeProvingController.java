package uk.gov.digital.ho.proving.income.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;


public abstract class AbstractIncomeProvingController {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected final String CONTENT_TYPE = "Content-type";
    protected final String APPLICATION_JSON = "application/json";


    protected abstract <T> T buildErrorResponse(HttpHeaders headers, String statusCode, String statusMessage, HttpStatus status);


    protected String sanitiseNino(String nino) {
        return nino.replaceAll("\\s", "").toUpperCase();
    }

    protected void validateNino(String nino) {
        final Pattern pattern = Pattern.compile("^[a-zA-Z]{2}[0-9]{6}[a-dA-D]{1}$");
        if (!pattern.matcher(nino).matches()) {
            throw new IllegalArgumentException("Invalid NINO");
        }
    }


}
