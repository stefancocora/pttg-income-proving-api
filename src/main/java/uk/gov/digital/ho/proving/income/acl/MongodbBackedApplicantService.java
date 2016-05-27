package uk.gov.digital.ho.proving.income.acl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import uk.gov.digital.ho.proving.income.domain.IncomeProvingResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * This class retrieves data from an external sources and converts it to Home Office domain classes. When the HMRC web
 * api is available this class will call the api via a delegate and then convert the response to Home Office
 * domain classes.
 */
public class MongodbBackedApplicantService implements IndividualService {
    @Autowired
    @Qualifier("applicantCollection")
    DBCollection applicantCollection;

    @Autowired
    private ObjectMapper mapper;

    private static Logger LOGGER = LoggerFactory.getLogger(MongodbBackedApplicantService.class);

//    // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public IncomeProvingResponse lookup(String nino, Date applicationFromDate, Date applicationToDate) {
        // TODO Make use of date range

        DBObject query = new QueryBuilder().start().put("individual.nino").is(nino).get();
        DBCursor cursor = applicantCollection.find(query);

        if (1 == cursor.size()) {
            JSONObject jsonResponse = new JSONObject(cursor.next().toString());
            jsonResponse.remove("_id");

            try {
                IncomeProvingResponse incomeProvingResponse = mapper.readValue(jsonResponse.toString(), IncomeProvingResponse.class);
                incomeProvingResponse.setIncomes(incomeProvingResponse.getIncomes().stream().filter( income ->
                    !(income.getPayDate().before(applicationFromDate)) && !(income.getPayDate().after(applicationToDate))
                ).collect(Collectors.toList()));
                LOGGER.info(incomeProvingResponse.toString());
                return incomeProvingResponse;
            } catch ( Exception e) {
                LOGGER.error("Could not map JSON from mongodb to Application domain class", e);
                // TODO change exception or replace with Optional
                throw new EarningsServiceFailedToMapDataToDomainClass();
            }

        } else {
            LOGGER.error("Could not retrieve a unique document from mongodb for criteria [" + nino + "]");
            // TODO change exception or replace with Optional
            throw new EarningsServiceNoUniqueMatch();
        }
    }
}
