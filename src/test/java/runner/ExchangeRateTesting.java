//rename to ExchangeRateTest to run tests  without BDD

package runner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.exception.APIError;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.questions.NumberOfCurrencyPairs;
import net.serenitybdd.screenplay.rest.questions.TheResponse;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.*;


@RunWith(SerenityRunner.class)
public class ExchangeRateTesting {

    Actor apiUser = Actor.named("API User").whoCan(CallAnApi.at("https://open.er-api.com/v6"));

    @Test
    public void api_response_time_not_less_than_3_seconds() {


        // Given
       Instant beforeRequest = Instant.now();

       // When
        apiUser.attemptsTo(
                Get.resource("/latest/USD")
       );
        apiUser.should(
              seeThat("The API response time", actor -> {
                   Instant afterRequest = Instant.now();
                    Duration duration = Duration.between(beforeRequest, afterRequest);
                    System.out.println(beforeRequest);
                    System.out.println(afterRequest);
                    System.out.println("API response time: " + duration.getSeconds() + " seconds");
                   return duration.getSeconds();
                }, lessThanOrEqualTo(3L))
                        .orComplainWith(APIError.class, "API response time was less than 3 seconds")
        );
    }

    @Test
    public void api_status() {
        // Given
        apiUser.attemptsTo(
                Get.resource("/latest/USD")
        );

        // Then
        apiUser.should(
                seeThat(TheResponse.statusCode(), equalTo(200))
        );

       }


    @Test
    public void fetch_USD_to_AED_rate_within_range() {
        // Given
        apiUser.attemptsTo(
                Get.resource("/latest/USD")
        );

        // Then
        apiUser.should(
                seeThat("The USD to AED rate",
                        actor -> SerenityRest.lastResponse().jsonPath().getDouble("rates.AED"),
                        both(greaterThanOrEqualTo(3.6)).and(lessThanOrEqualTo(3.7))
                )
                        .orComplainWith(APIError.class, "USD to AED rate is not within the range of 3.6 to 3.7")
        );

        // And
        apiUser.should(
                seeThatResponse("The API response",
                        response -> response.statusCode(lessThanOrEqualTo(400))
                )
        );
    }



    @Test
    public void check_API_response_status() {
        // Given
        Actor apiUser = Actor.named("API User").whoCan(CallAnApi.at("https://open.er-api.com/v6"));

        // When - Making API call for USD
        apiUser.attemptsTo(
                Get.resource("/latest/USD")
        );

        // Then - Verify response for USD
        apiUser.should(
                seeThatResponse("The API response for USD",
                        response -> response.statusCode(is(equalTo(200))) // Assuming 200 is returned for successful response
                                .and().body("result", is(equalTo("success")))
                )
        );

        // When - Making API call with incorrect endpoint (to simulate failure scenario)
        apiUser.attemptsTo(
                Get.resource("/latest/USD123") // Assuming this is an incorrect endpoint
        );

        // Then - Verify response for incorrect endpoint
        apiUser.should(
                seeThatResponse("The API response for incorrect endpoint",
                        response -> response.statusCode(is(equalTo(404))) // Assuming 404 is returned for incorrect endpoint
                                .and().body(containsString("<h1>404 Not Found</h1>")) // Matching the HTML content for "404 Not Found"
                                .and().body(containsString("<title>404 Not Found</title>")) // Matching the HTML title
                                .and().body(containsString("<center>nginx</center>")) // Matching the HTML footer
                )
        );
    }







    @Test
    public void verify_162_currency_pairs_returned() {
        // Given
        apiUser.attemptsTo(
                Get.resource("/latest/USD")
        );

        // Then

        apiUser.should(
                seeThat("The number of currency pairs", new NumberOfCurrencyPairs(), equalTo(162))
        );
    }

    @Test
    public void verify_api_response_matches_json_schema() throws IOException, ProcessingException {
        // Given
        apiUser.attemptsTo(
                Get.resource("/latest/USD")
        );

        // Extract raw response body as a String
        String responseBody = SerenityRest.lastResponse().asString();

        // Parse the JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);

        // Load the JSON schema template
        InputStream schemaInputStream = getClass().getResourceAsStream("/schema_template.json");

        JSONTokener schemaToken = new JSONTokener(schemaInputStream);
        JSONObject schemaJson = new JSONObject(schemaToken);

        // Create a JSON schema from the loaded schema JSON
        Schema schema = SchemaLoader.load(schemaJson);

        // Validate the API response against the JSON schema
        try {
            schema.validate(responseJson);
        } catch (Exception e) {
            // Handle validation errors here
            e.printStackTrace();
        }
    }
}


//        // Then
//        Schema schema = JsonSchemaGenerator.generateSchema();
//        apiUser.should(
//                seeThat("The API response matches JSON schema", APIMatchesJsonSchema.matchesJsonSchema(schema))
//                        .orComplainWith(APIError.class, "API response does not match JSON schema")
//        );
//    }

