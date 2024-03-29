package tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.*;

public class VerifyResponseStatus implements Task {

    private final int expectedStatusCode;
    private final String endpoint;
    private final String expectedContent;

    public VerifyResponseStatus(int expectedStatusCode, String endpoint, String expectedContent) {
        this.expectedStatusCode = expectedStatusCode;
        this.endpoint = endpoint;
        this.expectedContent = expectedContent;
    }

    public static VerifyResponseStatus is(int expectedStatusCode) {
        return new VerifyResponseStatus(expectedStatusCode, null, null);
    }

    public static VerifyResponseStatus forEndpoint(int expectedStatusCode, String endpoint) {
        return new VerifyResponseStatus(expectedStatusCode, endpoint, null);
    }

    public static VerifyResponseStatus forEndpointWithError(int expectedStatusCode, String endpoint, String expectedContent) {
        return new VerifyResponseStatus(expectedStatusCode, endpoint, expectedContent);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        int actualStatusCode = lastResponse().statusCode();
        System.out.println("Actual status code: " + actualStatusCode);

        if (endpoint == null) {
            actor.should(
                    seeThatResponse("The API response",
                            response -> response.statusCode(lessThanOrEqualTo(expectedStatusCode))
                    )
            );
        } else {
            actor.should(
                    seeThatResponse("The response status code for " + endpoint,
                            response -> response.statusCode(equalTo(expectedStatusCode))
                    )
            );
            if (expectedStatusCode != 200 && expectedContent != null) {
                actor.should(
                        seeThat("The response body for " + endpoint,
                                actor1 -> lastResponse().asString(),
                                containsString(expectedContent)
                        )
                );
            }
        }
    }
}
