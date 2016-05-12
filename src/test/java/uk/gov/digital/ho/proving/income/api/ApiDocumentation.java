package uk.gov.digital.ho.proving.income.api;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.gov.digital.ho.proving.income.ServiceRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@SpringApplicationConfiguration(classes = ServiceRunner.class)
@WebIntegrationTest("server.port=0")
@RunWith(SpringJUnit4ClassRunner.class)
public class ApiDocumentation {

    private RequestSpecification spec;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Value("${local.server.port}")
    private int port;

    private ResponseFieldsSnippet individualResponseFields =
            responseFields( // to do write good documentation
                    fieldWithPath("individual").description("The individual who is the subject of this financial status"),
                    fieldWithPath("individual.nino").description("The individual's NINO"),
                    fieldWithPath("individual.forename").description("The individual's forename which may be blah"),
                    fieldWithPath("individual.surname").description("The individual's surname except when nope")
            );

    @Before
    public void setUp() {
        this.spec =
                new RequestSpecBuilder()
                        .addFilter(documentationConfiguration(this.restDocumentation))
                        .build();
    }

    @Test
    public void statusNinoExample() throws Exception {

        // to do - write good documentation

        given(this.spec)
                .accept("application/json") // to do - do this once in setup
                .param("applicationRaisedDate", "2015-09-23")
                .filter(
                        document("status-nino-example", // to do - use convention in setup as per mockmvc example
                                this.individualResponseFields.and(
                                        // to do - factor out common categoryCheck fields as per individual
                                        fieldWithPath("categoryCheck").description("The financial status category check details"),
                                        fieldWithPath("categoryCheck.category").description("The category for the category check"),
                                        fieldWithPath("categoryCheck.passed").description("True if this check was satisfied, otherwise false"),
                                        fieldWithPath("categoryCheck.applicationRaisedDate").description("to do"),
                                        fieldWithPath("categoryCheck.assessmentStartDate").description("to do"),
                                        fieldWithPath("categoryCheck.failureReason").description("to do"),

                                        fieldWithPath("status").description("Information about the status of this response")
                                ),
                                requestParameters(
                                        parameterWithName("applicationRaisedDate").description("How would you describe what this means? Formatted as yyyy-mm-dd eg 2015-09-23"),

                                        parameterWithName("dependants").description("Number of dependants declared at time of application. Optional. Must be 0 or higher.")
                                                .optional() // to do - manually template optional until bug is fixed
                                ),
                                pathParameters(
                                        parameterWithName("nino").description("The applicant's NINO")
                                )
                        ))

                .when().port(this.port) // to do - do this once in setup
                .get("/incomeproving/v1/individual/{nino}/financialstatus", "AA123456A") // to do - factor out version to setup config
                // to do - configure scheme/host/port for documentation as per mockmvc example
                .then().assertThat().statusCode(is(200));


    }
}
