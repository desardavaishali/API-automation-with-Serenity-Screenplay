package tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import response.ExchangeRateResponse;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import java.io.IOException;

public class DeserializeExchangeRateResponse implements Task {
    private ExchangeRateResponse exchangeRateResponse;

    @Override
    public <T extends Actor> void performAs(T actor) {
        String responseBody = actor.recall("responseBody");

        if (responseBody == null || responseBody.isEmpty()) {
            throw new RuntimeException("Response body is null or empty");
        }

        ObjectMapper objectMapper = new ObjectMapper();
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
