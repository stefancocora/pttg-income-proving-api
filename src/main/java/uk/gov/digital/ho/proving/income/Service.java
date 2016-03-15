package uk.gov.digital.ho.proving.income;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

@RestController
public class Service {

    @RequestMapping(value="/application", method= RequestMethod.GET)
    public Application getTemporaryMigrationFamilyApplication(@RequestParam(value="nino", required=false) String nino) {
        Applicant applicant = new Applicant("Mr", "Brian", "Snail", nino);
        TemporaryMigrationFamilyApplication application = new TemporaryMigrationFamilyApplication(applicant, "A", true, new BigDecimal(18600.00), new Date());
        return application;
    }

}