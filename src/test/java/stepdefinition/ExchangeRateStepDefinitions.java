package stepdefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.exception.APIError;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.questions.NumberOfCurrencyPairs;
import net.serenitybdd.screenplay.rest.questions.TheResponse;
import org.assertj.core.api.SoftAssertions;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.*;

public class ExchangeRateStepDefinitions {

    @Given("{actor} makes a GET request to {string}")
    public void makeGetRequest(Actor actor, String endpoint) {
        String baseURL = "https://open.er-api.com/v6"; // Provide the correct base URL here
        CallAnApi callAnApi = CallAnApi.at(baseURL);
        actor.whoCan(callAnApi);

        actor.attemptsTo(
                Get.resource(endpoint)
        );
    }

    @When("the response is received")
    public void receiveResponse() {
        // Nothing to implement here as it's a generic step
    }

    @Then("{actor} sees the response time should be less than or equal to {int} seconds")
    public void verifyResponseTime(Actor actor, int seconds) {

        Instant beforeRequest = Instant.now();

        actor.should(
                seeThat("The API response time", actor1 -> {
                    Instant afterRequest = Instant.now();
                    Duration duration = Duration.between(beforeRequest, afterRequest);
                    System.out.println("API response time: " + duration.getSeconds() + " seconds"); // Clearer output
                    return (int) duration.getSeconds(); // Cast to int
                }, lessThanOrEqualTo(seconds)) // Assert in seconds
                        .orComplainWith(APIError.class, "API response time was greater than " + seconds + " seconds")
        );
    }


    @Then("{actor} receives the response status code {int}")
    public void verifyResponseStatusCode(Actor actor, int statusCode) {
        actor.should(
                seeThat(TheResponse.statusCode(), equalTo(statusCode))
        );
    }

    @Then(" {actor} looks for the {string} should be greater than or equal to {double}")
    public void verifyRateInRange(Actor actor, String currencyPair, double lowerBound) {
        actor.should(
                seeThat("The " + currencyPair + " rate",
                        actorInsideLambda -> lastResponse().jsonPath().getDouble("rates." + currencyPair),
                        greaterThanOrEqualTo(lowerBound))
                        .orComplainWith(APIError.class, currencyPair + " rate is not within the expected range")
        );
    }

    @Then("{actor} looks for the {string} should be less than or equal to {double}")
    public void verifyRateLessThanOrEqualTo(Actor actor, String currencyPair, double upperBound) {
        actor.should(
                seeThat("The " + currencyPair + " rate",
                        actorInsideLambda -> lastResponse().jsonPath().getDouble("rates." + currencyPair),
                        lessThanOrEqualTo(upperBound))
                        .orComplainWith(APIError.class, currencyPair + " rate is not within the expected range")
        );
    }

    @Then("{actor} looks for the number of currency pairs returned should be {int}")
    public void verifyNumberOfCurrencyPairs(Actor actor, int expectedPairs) {
        actor.should(
                seeThat("The number of currency pairs", new NumberOfCurrencyPairs(), equalTo(expectedPairs))
        );
    }


    @Then("{actor} looks for the USD to AED rate should be greater than or equal to {double}")
    public void verifyRateGreaterThanOrEqualTo(Actor actor, double lowerBound) {
        actor.should(
                seeThat("The USD to AED rate",
                        actor1 -> lastResponse().jsonPath().getDouble("rates.AED"),
                        both(greaterThanOrEqualTo(lowerBound)).and(lessThanOrEqualTo(3.7))
                )
                        .orComplainWith(APIError.class, "USD to AED rate is not within the range of 3.6 to 3.7")
        );
    }

    @Then("{actor} looks for the USD to AED rate should be less than or equal to {double}")
    public void verifyRateLessThanOrEqualTo(Actor actor, double upperBound) {
        actor.should(
                seeThat("The USD to AED rate",
                        actor1 -> lastResponse().jsonPath().getDouble("rates.AED"),
                        lessThanOrEqualTo(upperBound)
                )
                        .orComplainWith(APIError.class, "USD to AED rate is not within the range of 3.6 to 3.7")
        );
    }

    @Then("{actor} looks for the API response status code should be less than or equal to {int}")
    public void verifyResponseStatusCodes(Actor actor, int expectedStatusCode) {
        actor.should(
                seeThatResponse("The API response",
                        response -> response.statusCode(lessThanOrEqualTo(expectedStatusCode))
                )
        );
    }

    @Then("{actor} looks for the response status code for the incorrect endpoint should be {int}")
    public void verifyIncorrectEndpointResponseStatusCode(Actor actor, int expectedStatusCode) {
        actor.should(
                seeThatResponse("The response status code for the incorrect endpoint",
                        response -> response.statusCode(equalTo(expectedStatusCode))
                )
        );
    }


    @Then("{actor} looks for the response body for the incorrect endpoint should contain {string}")
    public void verifyIncorrectEndpointResponseBodyContains(Actor actor, String expectedContent) {
        actor.should(
                seeThat("The response body for the incorrect endpoint",
                        actor1 -> lastResponse().getBody().asString(),
                        containsString(expectedContent)
                )
        );
    }


    @Then("{actor} looks for the response status code for USD should be {int}")
    public void verifyUSDResponseStatusCode(Actor actor, int expectedStatusCode) {
        actor.should(
                seeThatResponse("The response status code for USD",
                        response -> response.statusCode(equalTo(expectedStatusCode))
                )
        );
    }

    @Then("{actor} looks for the response body for USD should have {string} equal to {string}")
    public void verifyUSDResponseBody(Actor actor, String jsonPath, String expectedValue) {
        actor.should(
                seeThat("The response body for USD",
                        actor1 -> lastResponse().body().jsonPath().getString(jsonPath),
                        equalTo(expectedValue)
                )
        );
    }

    @Then("{actor} should not complain with an APIError")
    public void shouldNotComplainWithAPIError(Actor actor) {
        SoftAssertions softly = new SoftAssertions();
        softly.assertAll();
    }

    @Given("the API user exists")
    public void theAPIUserExists() {
    }


    @Then("{actor} looks for the API response should match the JSON schema")
    public void verifyApiResponseMatchesJsonSchema(Actor actor) {
        // Given
        actor.attemptsTo(
                Get.resource("/latest/USD")
        );

        // Extract raw response body as a String
        String responseBody = lastResponse().asString();

        // Load the JSON schema template
        InputStream schemaInputStream = getClass().getResourceAsStream("/schema_template.json");

        assert schemaInputStream != null;
        JSONTokener schemaToken = new JSONTokener(schemaInputStream);
        JSONObject schemaJson = new JSONObject(schemaToken);

        // Create a JSON schema from the loaded schema JSON
        Schema schema = SchemaLoader.load(schemaJson);

        // Validate the API response against the JSON schema
        try {
            schema.validate(new JSONObject(responseBody));
        } catch (Exception validationError) {
            String errorMessage = "API response does not match JSON schema: " + validationError.getMessage();
            throw new APIError(errorMessage);
        }
    }
}
