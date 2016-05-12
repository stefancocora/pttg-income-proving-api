package uk.gov.digital.ho.proving.income.api;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.gov.digital.ho.proving.income.ServiceRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;

@SpringApplicationConfiguration(classes = ServiceRunner.class)
@WebIntegrationTest("server.port=0")
@RunWith(SpringJUnit4ClassRunner.class)
public class ApiDocumentation {

    @Rule
    public JUnitRestDocumentation restDocumentationRule = new JUnitRestDocumentation("build/generated-snippets");

    @Value("${local.server.port}")
    private int port;

    private RequestSpecification documentationSpec;

    private RequestSpecification requestSpec;

    private RestDocumentationFilter document = document(
            "{method-name}",
            preprocessRequest(modifyUris()
                    .scheme("https")
                    .host("api.host.address")
                    .removePort())
    );

    private FieldDescriptor[] individualModelFields = new FieldDescriptor[]{
            // to do write good documentation
            fieldWithPath("individual").description("The individual who is the subject of this financial status"),
            fieldWithPath("individual.nino").description("The individual's NINO"),
            fieldWithPath("individual.forename").description("The individual's forename which may be blah"),
            fieldWithPath("individual.surname").description("The individual's surname except when nope")
    };

    private FieldDescriptor[] categoryCheckModelFields = new FieldDescriptor[]{
            // to do write good documentation
            fieldWithPath("categoryCheck").description("The financial status category check details"),
            fieldWithPath("categoryCheck.category").description("The category for the category check"),
            fieldWithPath("categoryCheck.passed").description("True if this check was satisfied, otherwise false"),
            fieldWithPath("categoryCheck.applicationRaisedDate").description("to do"),
            fieldWithPath("categoryCheck.assessmentStartDate").description("to do"),
            fieldWithPath("categoryCheck.failureReason").description("to do")
    };

    private FieldDescriptor[] statusModelFields = new FieldDescriptor[]{
            // to do write good documentation
            fieldWithPath("status").description("todo"),
            fieldWithPath("status.code").description("todo"),
            fieldWithPath("status.message").description("todo")
    };

    @Before
    public void setUp() {

        RestAssured.port = this.port;
        RestAssured.basePath = "/incomeproving/v1";

        requestSpec = new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .build();

        this.documentationSpec =
                new RequestSpecBuilder()
                        .addFilter(documentationConfiguration(this.restDocumentationRule))
                        .addFilter(document)
                        .build();
    }

    @Test
    public void statusNinoExample() throws Exception {

        // to do - write good documentation

        given(documentationSpec)
                .spec(requestSpec)
                .param("applicationRaisedDate", "2015-09-23")
                .filter(document.snippets(
                        responseFields(individualModelFields)
                                .and(categoryCheckModelFields)
                                .and(statusModelFields),
                        requestParameters(
                                parameterWithName("applicationRaisedDate")
                                        .description("How would you describe what this means? Formatted as yyyy-mm-dd eg 2015-09-23"),
                                parameterWithName("dependants")
                                        .description("Number of dependants declared at time of application. Optional. Must be 0 or higher.")
                                        .optional() // to do - manually template optional until bug is fixed
                        ),
                        pathParameters(
                                parameterWithName("nino")
                                        .description("The applicant's NINO")
                        )
                ))

                .when().get("/individual/{nino}/financialstatus", "AA123456A")
                .then().assertThat().statusCode(is(200));
    }
}
