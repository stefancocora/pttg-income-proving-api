package uk.gov.digital.ho.proving.income.acl;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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

    @Override
    public String lookup(String nino) {
        DBObject query = new QueryBuilder().start().put("applicant.nino").is(nino).get();
        DBCursor cursor = applicationsCollection.find(query);

        if (1 == cursor.size()) {
            JSONObject jsonResponse = new JSONObject(cursor.next().toString());
            jsonResponse.remove("_id");
            return jsonResponse.toString();
        } else {
            throw new RuntimeException("did not find a unique match");
        }
    }

}
