package uk.gov.digital.ho.proving.income.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.proving.income.domain.Applicant;
import uk.gov.digital.ho.proving.income.domain.Application;
import uk.gov.digital.ho.proving.income.domain.TemporaryMigrationFamilyApplication;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

@RestController
public class Service {

    @RequestMapping(value="/application", method= RequestMethod.GET)
    public ResponseEntity<Application> getTemporaryMigrationFamilyApplication(@RequestParam(value="nino", required=false) String nino) {
        Logger logger = LoggerFactory.getLogger(Service.class);
        logger.info("*************** Income Proving Service API starting.");

        Application application = null;

        if (nino.equalsIgnoreCase("XXX")) {
            return new ResponseEntity<Application>(buildApplication(nino), HttpStatus.OK);
        }

        String responseString;

        try {
            responseString = lookup(nino);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type","application/json");

            ObjectMapper mapper  = new ObjectMapper();

            Application obj = mapper.readValue(responseString, TemporaryMigrationFamilyApplication.class);

            return new ResponseEntity<Application>(obj, headers, HttpStatus.OK);
        } catch (JsonMappingException | JsonParseException e) {
            logger.error("Error building response.", e);
        } catch (IOException e) {
            logger.error("Error building response.", e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Application buildApplication(String nino) {
        Applicant applicant = new Applicant("Mr", "Brian", "Snail", nino);

        TemporaryMigrationFamilyApplication application = new TemporaryMigrationFamilyApplication(applicant, new Date(), "A", true, new BigDecimal(18600.00));
        return application;
    }

    private String lookup(String nino) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");

        DBCollection coll = mongoClient.getDB("test").getCollection("applications");
        DBObject query = new QueryBuilder().start().put("applicant.nino").is(nino).get();

        DBCursor cursor = coll.find(query);

        if (1 == cursor.size()) {
            JSONObject jsonResponse = new JSONObject(cursor.next().toString());
            jsonResponse.remove("_id");
            return jsonResponse.toString();
        } else {
            throw new RuntimeException("did not find a unique match");
        }
    }

}