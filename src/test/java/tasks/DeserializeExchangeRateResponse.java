package tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import response.ExchangeRateResponse;

import java.io.IOException;

    public class DeserializeExchangeRateResponse implements Task {

        @Override
        public <T extends Actor> void performAs(T actor) {
            String responseBody = SerenityRest.lastResponse().asString();

            if (responseBody == null || responseBody.isEmpty()) {
                throw new RuntimeException("Response body is null or empty");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            ExchangeRateResponse exchangeRateResponse;
            try {
                exchangeRateResponse = objectMapper.readValue(responseBody, ExchangeRateResponse.class);
            } catch (IOException e) {
                throw new RuntimeException("Error deserializing exchange rate response: " + e.getMessage(), e);
            }

            actor.remember("exchangeRateResponse", exchangeRateResponse);
        }

        public static DeserializeExchangeRateResponse fromPreviousResponse() {
            return new DeserializeExchangeRateResponse();
        }
    }

