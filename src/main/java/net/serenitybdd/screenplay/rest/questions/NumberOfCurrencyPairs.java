package net.serenitybdd.screenplay.rest.questions;


import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class NumberOfCurrencyPairs implements Question<Integer> {
    @Override

    public Integer answeredBy(Actor actor) {
        // Send a request to the API endpoint
        SerenityRest.when().get("https://open.er-api.com/v6/latest/USD");

        // Extract the response body and count the number of currency pairs
        int numberOfCurrencyPairs = SerenityRest.lastResponse().jsonPath().getMap("rates").size();

        // Return the number of currency pairs
        return numberOfCurrencyPairs;
    }
    public static NumberOfCurrencyPairs retrieved() {
        return new NumberOfCurrencyPairs();
    }
}
