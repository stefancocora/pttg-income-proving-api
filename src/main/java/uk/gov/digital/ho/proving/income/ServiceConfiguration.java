package uk.gov.digital.ho.proving.income;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import uk.gov.digital.ho.proving.income.acl.EarningsService;
import uk.gov.digital.ho.proving.income.acl.MongodbBackedEarningsService;

import java.text.SimpleDateFormat;

/**
 * Created by andrewmoores on 17/03/2016.
 */
@Configuration
public class ServiceConfiguration {
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
        b.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return b;
    }

    @Bean
    public ObjectMapper getMapper() {
        ObjectMapper m = new ObjectMapper();
        m.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        m.enable(SerializationFeature.INDENT_OUTPUT);
        return m;
    }

    @Bean
    public EarningsService getRevenueService() {
        return new MongodbBackedEarningsService();
    }

    @Bean(name="applicationsCollection")
    public DBCollection getApplicationsCollection() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");
        DBCollection coll = mongoClient.getDB("test").getCollection("applications");

        return coll;
    }

}
