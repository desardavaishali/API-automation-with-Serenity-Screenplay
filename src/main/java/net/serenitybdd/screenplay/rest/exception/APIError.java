package net.serenitybdd.screenplay.rest.exception;

public class APIError extends Error {

        public APIError(String message) {
            super(message);
        }

        public APIError(String message, Throwable cause) {
            super(message, cause);
        }
    }