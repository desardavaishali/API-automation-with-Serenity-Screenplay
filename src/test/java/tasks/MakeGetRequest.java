package tasks;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class MakeGetRequest implements Task {

    private final String endpoint;
    private final String baseURL;

    public MakeGetRequest(String endpoint, String baseURL) {
        this.endpoint = endpoint;
        this.baseURL = baseURL;
    }

    public static MakeGetRequest toEndpoint(String endpoint) {
        return new MakeGetRequest(endpoint, "https://open.er-api.com/v6");
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        CallAnApi callAnApi = CallAnApi.at(baseURL);
        actor.whoCan(callAnApi);

        actor.attemptsTo(
                Get.resource(endpoint)
        );

        String responseBody = SerenityRest.lastResponse().getBody().asString();
        actor.remember("responseBody", responseBody);
    }
}
