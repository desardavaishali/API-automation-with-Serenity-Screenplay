package tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;

import java.io.File;
import java.io.IOException;

public class ApiResponseValidator {

    public static void validateApiResponseAgainstSchema(Actor actor) {
        // Extract the JSON response data using SerenityRest
        String responseJson = SerenityRest.lastResponse().asString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseNode;
        try {
            // Parse the response JSON using ObjectMapper
            responseNode = mapper.readTree(responseJson);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing API response body", e);
        }

        JsonNode schemaNode;
        try {
            // Load the schema file using ObjectMapper
            schemaNode = mapper.readTree(new File("apiSchema.json"));
        } catch (IOException e) {
            throw new RuntimeException("Error reading schema file", e);
        }

        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonSchema schema;
        try {
            schema = factory.getJsonSchema(schemaNode);
        } catch (ProcessingException e) {
            throw new RuntimeException("Error creating JSON schema", e);
        }

        try {
            ProcessingReport report = schema.validate(responseNode);
            if (!report.isSuccess()) {
                throw new RuntimeException("API response does not match JSON schema: " + report);
            }
        } catch (ProcessingException e) {
            throw new RuntimeException("Error validating API response against schema", e);
        }

        // Get the list of required properties from the schema
        JsonNode requiredPropertiesNode = schemaNode.path("required");
        if (requiredPropertiesNode.isArray()) {
            for (JsonNode requiredProperty : requiredPropertiesNode) {
                String propertyName = requiredProperty.asText();
                if (!responseNode.has(propertyName)) {
                    throw new RuntimeException("Required property '" + propertyName + "' is missing from the API response.");
                }
            }
        }
    }
}
