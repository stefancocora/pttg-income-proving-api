package uk.gov.digital.ho.proving.income.acl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import uk.gov.digital.ho.proving.income.domain.Application;
import uk.gov.digital.ho.proving.income.domain.TemporaryMigrationFamilyApplication;

import java.io.IOException;

/**
 * This class retrieves data from an external sources and converts it to Home Office domain classes. When the HMRC web
 * api is available this class will call the api via a delegate and then convert the response to Home Office
 * domain classes.
 *
 */
public class MongodbBackedEarningsService implements EarningsService {
    @Autowired
    @Qualifier("applicationsCollection")
    DBCollection applicationsCollection;

    @Autowired
    private ObjectMapper mapper;


    @Override
    public Application lookup(String nino) {
        DBObject query = new QueryBuilder().start().put("applicant.nino").is(nino).get();
        DBCursor cursor = applicationsCollection.find(query);

        if (1 == cursor.size()) {
            JSONObject jsonResponse = new JSONObject(cursor.next().toString());
            jsonResponse.remove("_id");
            try {
                Application application = mapper.readValue(jsonResponse.toString(), TemporaryMigrationFamilyApplication.class);
                return application;
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new RuntimeException();
        } else {
            throw new RuntimeException("did not find a unique match");
        }
    }

}
