package uk.gov.digital.ho.proving.income;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import uk.gov.digital.ho.proving.income.acl.EarningsService;
import uk.gov.digital.ho.proving.income.acl.IndividualService;
import uk.gov.digital.ho.proving.income.acl.MongodbBackedApplicantService;
import uk.gov.digital.ho.proving.income.acl.MongodbBackedEarningsService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ServiceConfiguration {

    private static Logger LOGGER = LoggerFactory.getLogger(ServiceConfiguration.class);

    private String host;

    @Value("${mongodb.host}")
    private String mongodbHost;

    @Value("${mongodb.port}")
    private String mongodbPort;


    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
        b.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return b;
    }

    @Bean
    public ObjectMapper getMapper() {
        ObjectMapper m = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-M-d")));
        m.registerModule(javaTimeModule);
        m.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        m.enable(SerializationFeature.INDENT_OUTPUT);
        return m;
    }

    @Bean
    public EarningsService getRevenueService() {
        return new MongodbBackedEarningsService();
    }

    @Bean
    public IndividualService getApplicantService() {
        return new MongodbBackedApplicantService();
    }

    @Bean(name="applicationsCollection")
    public DBCollection getApplicationsCollection() {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");
        DBCollection coll = mongoClient.getDB("test").getCollection("applications");

        return coll;
    }

    @Bean(name="applicantCollection")
    public DBCollection getApplicantCollection() {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");
        DBCollection coll = mongoClient.getDB("test").getCollection("applicants");

        return coll;
    }

    //port will be ignored if host not specified
    private MongoClient getMongoClient() {
        boolean useHost = (mongodbHost != null && !mongodbHost.isEmpty());
        boolean usePort = (mongodbPort != null && !mongodbPort.isEmpty());
        MongoClient client;
        if (useHost) {
            if (usePort) {
                client = new MongoClient(mongodbHost, Integer.parseInt(mongodbPort));
            } else {
                client = new MongoClient(mongodbHost);
            }
        } else {
            client = new MongoClient();
        }
        LOGGER.info("MongoClient invoked using host[" + mongodbHost + "] and port [" + mongodbPort + "]");
        return client;
    }

}
