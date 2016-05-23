package steps

import com.jayway.restassured.response.Response
import cucumber.api.DataTable
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import net.thucydides.core.annotations.Managed

import static com.jayway.jsonpath.JsonPath.read
import static com.jayway.restassured.RestAssured.get

/**
 * Created by mitchell on 11/05/16.
 */
class ProvingThingsApiSteps {


    @Managed
    public Response resp
    String jsonAsString
    String nino
    String dependants=""
    String applicationRaisedDate


    public String tocamelcase(String g) {
        StringBuilder sbl = new StringBuilder()

        String firstString
        String nextString
        String finalString = null
        char firstChar

        String[] f = g.split(" ")

        for (int e = 0; e < f.length; e++) {

            if (e == 0) {
                firstString = f[0].toLowerCase()
                sbl.append(firstString)

            }

            if (e > 0) {
                nextString = f[e].toLowerCase()
                firstChar = nextString.charAt(0)
                nextString = nextString.replaceFirst(firstChar.toString(), firstChar.toString().toUpperCase())
                sbl.append(nextString)
            }
            finalString = sbl.toString()

        }
        return finalString

    }

    def String getTableData(DataTable arg) {
        //TODO refactor to reject\identify unrecognised keys

        Map<String, String> entries = arg.asMap(String.class, String.class)
        String[] tableKey = entries.keySet()

        for (String s : tableKey) {

            if (s.equalsIgnoreCase("application raised date")) {
                applicationRaisedDate = entries.get(s)
            }
            if (s.equalsIgnoreCase("nino")) {
                nino = entries.get(s)
            }
            if (s.equalsIgnoreCase("dependants")) {
                dependants = entries.get(s)
            }
        }
    }

    /**
     prerequisites:
     - BDD key can be transformed to valid jsonpath OR key name has been added to FeatureKeyMapper.java
     - Date values are in the format yyyy-mm-dd
     - boolean values are lowercase
     */
    public void validateJsonResult(DataTable arg) {
        Map<String, String> entries = arg.asMap(String.class, String.class);
        String[] tableKey = entries.keySet();

        for (String key : tableKey) {
            switch (key) {
                case "HTTP Status":
                    assert entries.get(key) == resp.getStatusCode().toString();
                    break;
                default:
                    String jsonPath = FeatureKeyMapper.buildJsonPath(key);
                    assert entries.get(key) == read(jsonAsString, jsonPath).toString();
            }
        }
    }

    @Given("^A service is consuming the Income Proving TM Family API\$")
    public void a_service_is_consuming_the_Income_Proving_TM_Family_API() {

    }

    @When("^the Income Proving TM Family API is invoked with the following:\$")
    public void the_Income_Proving_TM_Family_API_is_invoked_with_the_following(DataTable expectedResult) {


        getTableData(expectedResult)
        resp = get("http://localhost:8081/incomeproving/v1/individual/{nino}/financialstatus?applicationRaisedDate={applicationRaisedDate}&dependants={dependants}", nino, applicationRaisedDate, dependants);
        jsonAsString = resp.asString();
        println "" + jsonAsString
    }

    @Then("^The Income Proving TM Family API provides the following result:\$")
    public void the_Income_Proving_TM_Family_API_provides_the_following_result(DataTable arg1) {
        validateJsonResult(arg1)

    }

    @When("^the Income Proving API is invoked with the following:\$")
    public void the_Income_Proving_API_is_invoked_with_the_following(DataTable arg1) throws Throwable {

    }

    @Then("^The API provides the following Individual details:\$")
    public void the_API_provides_the_following_Individual_details(DataTable arg1) throws Throwable {

    }

    @Then("^The API provides the following result:\$")
    public void the_API_provides_the_following_details(DataTable arg1) throws Throwable {

    }


}
