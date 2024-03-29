package tasks;


import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import java.time.Duration;
import java.time.Instant;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class VerifyResponseTime implements Task {

    private final int seconds;

    public VerifyResponseTime(int seconds) {
        this.seconds = seconds;
    }

    public static VerifyResponseTime isLessThanOrEqualTo(int seconds) {
        return new VerifyResponseTime(seconds);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Instant beforeRequest = Instant.now();

        actor.should(
                seeThat("The API response time",
                        actor1 -> {
                            Instant afterRequest = Instant.now();
                            Duration duration = Duration.between(beforeRequest, afterRequest);
                            System.out.println("API response time: " + duration.getSeconds() + " seconds"); // Clearer output
                            return (int) duration.getSeconds(); // Cast to int
                        },
                        lessThanOrEqualTo(seconds)) // Assert in seconds
        );
    }
}
