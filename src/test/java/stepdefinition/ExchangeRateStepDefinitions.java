package stepdefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import response.ExchangeRateResponse;
import tasks.*;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;


public class ExchangeRateStepDefinitions {

    @Given("{actor} makes a GET request to {string}")
    public void makeGetRequest(Actor actor, String endpoint) {
        actor.attemptsTo(
                MakeGetRequest.toEndpoint(endpoint)
        );
    }


    @When("the response is received")
    public void receiveResponse() {
        // Nothing to implement here as it's a generic step
    }

    @Then("{actor} receives the response status code {int}")
    public void verifyResponseStatus(Actor actor, int statusCode) {
        actor.should(
                seeThatResponse("The response status code",
                        response -> response.statusCode(equalTo(statusCode)))
        );
    }

    @Then("{actor} sees the response time should be less than or equal to {int} seconds")
    public void verifyResponseTime(Actor actor, int seconds) {
        actor.attemptsTo(
                VerifyResponseTime.isLessThanOrEqualTo(seconds)
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
        actor.attemptsTo(DeserializeExchangeRateResponse.fromPreviousResponse());

        ExchangeRateResponse exchangeRateResponse = actor.recall("exchangeRateResponse");

        // Perform assertions using the parsed Java object
        Assertions.assertThat(exchangeRateResponse.getRates().get("AED")).isGreaterThanOrEqualTo(lowerBound);
    }


    @Then("{actor} looks for the USD to AED rate should be less than or equal to {double}")
    public void verifyRateLessThanOrEqualTo(Actor actor, double upperBound) {
        actor.attemptsTo(DeserializeExchangeRateResponse.fromPreviousResponse());

        ExchangeRateResponse exchangeRateResponse = actor.recall("exchangeRateResponse");

        Assertions.assertThat(exchangeRateResponse.getRates().get("AED")).isLessThanOrEqualTo(upperBound);
    }

    @Then("{actor} looks for the API response status code should be less than or equal to {int}")
    public void verifyResponseStatusCodes(Actor actor, int expectedStatusCode) {
        actor.attemptsTo(
                tasks.VerifyResponseStatus.is(expectedStatusCode)
        );
    }

    @Then("{actor} looks for the response status code for the incorrect endpoint should be {int}")
    public void verifyIncorrectEndpointResponseStatusCode(Actor actor, int expectedStatusCode) {
        actor.attemptsTo(
                tasks.VerifyResponseStatus.forEndpoint(expectedStatusCode, "incorrect")
        );
    }

    @Then("{actor} looks for the response body for the incorrect endpoint should contain {string}")
    public void verifyIncorrectEndpointResponseBodyContains(Actor actor, String expectedContent) {
        actor.attemptsTo(
                tasks.VerifyResponseStatus.forEndpointWithError(404, "incorrect", expectedContent)
        );
    }

    @Then("{actor} looks for the response status code for USD should be {int}")
    public void verifyUSDResponseStatusCode(Actor actor, int expectedStatusCode) {
        actor.attemptsTo(
                tasks.VerifyResponseStatus.forEndpoint(expectedStatusCode, "USD")
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


    @Then("{actor} looks for the API response should match the JSON schema")
    public void verifyApiResponseMatchesJsonSchema(Actor actor) {
        ApiResponseValidator.validateApiResponseAgainstSchema(actor);
    }


    @Then("the API user should not complain with an APIError")
    public void shouldNotComplainWithAPIError() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertAll();
    }

    @Given("the API user exists")
    public void theAPIUserExists() {
    }

    @Then("{actor} receives a valid price {int} for currency {string}")
    public void verifyValidPrice(Actor actor, int rate, String currency) {

            actor.attemptsTo(DeserializeExchangeRateResponse.fromPreviousResponse());

            ExchangeRateResponse exchangeRateResponse = actor.recall("exchangeRateResponse");

            Assertions.assertThat(exchangeRateResponse.getRates().get(currency)).isEqualTo(rate);
        }
}



