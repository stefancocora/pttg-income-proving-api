package steps

import com.jayway.restassured.response.Response
import cucumber.api.DataTable
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import net.thucydides.core.annotations.Managed
import org.json.JSONObject

import static com.jayway.restassured.RestAssured.get

/**
 * Created by mitchell on 11/05/16.
 */
class ProvingThingsApiSteps {

    @Managed
    public Response resp
    JSONObject jsonResponse
    String jsonAsString
    String nino
    String dependants = " "
    String applicationRaisedDate
    String[] s


    public String tocamelcase(String g) {
        StringBuilder sbl = new StringBuilder()

        String firstString
        String nextString
        String combinedString
        String finalString
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


    def changeDateFormat(String oldDateFormat){

        Date date =  Date.parse( 'dd/mm/yyyy', oldDateFormat)
        String newFormat = date.format("yyyy-mm-dd")
        println "" + newFormat
        newFormat

    }

    def String getTableData(DataTable arg){

        Map<String, String> entries = arg.asMap(String.class, String.class)
        String[] tableKey = entries.keySet()

        for(String s:tableKey){

            if(s.equalsIgnoreCase("application raised date")){
                applicationRaisedDate = changeDateFormat(entries.get(s))
            }
            if(s.equalsIgnoreCase("nino")){

                nino = entries.get(s)
            }
            if(s.equalsIgnoreCase("dependants")){
                dependants = entries.get(s)
            }
        }
    }

    def validateResult(DataTable arg){
        Map<String, String> entries = arg.asMap(String.class, String.class)
        String[] tableKey = entries.keySet()

        jsonAsString = resp.asString();
        JSONObject json = new JSONObject(jsonAsString);

        for(String s : tableKey){

            if(s.equalsIgnoreCase("status code")){
                assert entries.get(s) == resp.getStatusCode().toString()
            }

            if(s.equalsIgnoreCase("outcome box individual forename")){
                assert entries.get(s) == json.getJSONObject("individual").getString("forename")
            }


        }

    }


    @Given("^A service is consuming the Income Proving TM Family API\$")
    public void a_service_is_consuming_the_Income_Proving_TM_Family_API()  {

    }

    @When("^the Income Proving TM Family API is invoked with the following:\$")
    public void the_Income_Proving_TM_Family_API_is_invoked_with_the_following(DataTable expectedResult){


        getTableData(expectedResult)
               resp = get("http://localhost:8081/incomeproving/v1/individual/"+nino+"/financialstatus?applicationRaisedDate="+applicationRaisedDate+"&dependants="+dependants)

              jsonAsString = resp.asString();
             println ""+ jsonAsString
    }

    @Then("^The Income Proving TM Family API provides the following result:\$")
    public void the_Income_Proving_TM_Family_API_provides_the_following_result(DataTable arg1) {

    }



}
