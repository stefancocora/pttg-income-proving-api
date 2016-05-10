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
import uk.gov.digital.ho.proving.income.domain.Application;
import uk.gov.digital.ho.proving.income.domain.TemporaryMigrationFamilyApplication;

import java.io.IOException;
import java.util.Date;

/**
 * This class retrieves data from an external sources and converts it to Home Office domain classes. When the HMRC web
 * api is available this class will call the api via a delegate and then convert the response to Home Office
 * domain classes.
 */
public class MongodbBackedEarningsService implements EarningsService {
    @Autowired
    @Qualifier("applicationsCollection")
    DBCollection applicationsCollection;

    @Autowired
    private ObjectMapper mapper;

    private static Logger LOGGER = LoggerFactory.getLogger(MongodbBackedEarningsService.class);

    @Override
    public Application lookup(String nino, Date applicationRaisedDate) {
        DBObject query = new QueryBuilder().start().put("individual.nino").is(nino).get();
        DBCursor cursor = applicationsCollection.find(query);

        if (1 == cursor.size()) {
            try {
                JSONObject jsonResponse = new JSONObject(cursor.next().toString());
                jsonResponse.remove("_id");

                Application application = mapper.readValue(jsonResponse.toString(), TemporaryMigrationFamilyApplication.class);
                return application;
            } catch (JSONException | IOException e) {
                LOGGER.error("Could not map JSON from mongodb to Application domain class", e);
                throw new EarningsServiceFailedToMapDataToDomainClass();
            }
        } else {
            LOGGER.error("Could not retrieve a unique document from mongodb for criteria [" + nino + "]");
            throw new EarningsServiceNoUniqueMatch();
        }
    }
}
