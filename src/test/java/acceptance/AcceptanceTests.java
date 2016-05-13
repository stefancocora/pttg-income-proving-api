package acceptance;
/**
 * Created by mitchell on 11/05/16.
 */

import cucumber.api.*;
import net.serenitybdd.cucumber.*;
import org.junit.runner.*;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features={"src/test/specs/API-v1"}
        , glue={"steps"})

public class AcceptanceTests {
}
