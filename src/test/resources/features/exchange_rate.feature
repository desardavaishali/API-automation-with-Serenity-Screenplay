Feature: Exchange Rate API


  @statusCode
  Scenario: Verify API status code is 200
    Given the API user makes a GET request to '/latest/USD'
    Then the API user receives the response status code 200


  @verifyPrice
  Scenario: Verify API call is successful and returns valid price
    Given the API user makes a GET request to '/latest/USD'
    When the response is received
    Then the API user receives the response status code 200
    And the API user receives a valid price 1 for currency 'USD'


  @validAndIncorrectEndpoints
  Scenario: Verify API response for valid and incorrect endpoints
    Given the API user exists
    When the user makes a GET request to '/latest/USD'
    Then he looks for the response status code for USD should be 200
    And he looks for the response body for USD should have "result" equal to "success"

    When the user makes a GET request to "/latest/USD123"
    Then he looks for the response status code for the incorrect endpoint should be 404
    And he looks for the response body for the incorrect endpoint should contain "<h1>404 Not Found</h1>"
    And he looks for the response body for the incorrect endpoint should contain "<title>404 Not Found</title>"
    And he looks for the response body for the incorrect endpoint should contain "<center>nginx</center>"

  @rateRange
  Scenario: Verify USD to AED rate is within a specific range
    Given the API user makes a GET request to '/latest/USD'
    Then the API user looks for the USD to AED rate should be greater than or equal to 3.6
    And the API user looks for the USD to AED rate should be less than or equal to 3.7
    And he looks for the API response status code should be less than or equal to 400


  @responseTime
  Scenario: Verify API response time is less than 3 seconds
    Given apiUser makes a GET request to '/latest/USD'
    When the response is received
    Then apiUser sees the response time should be less than or equal to 3 seconds
    And  the API user should not complain with an APIError


  @currencyPairs
  Scenario: Verify 162 currency pairs are returned
    Given the API user makes a GET request to '/latest/USD'
    Then he looks for the number of currency pairs returned should be 162

  @jsonSchema
  Scenario: Verify API response matches JSON schema
    Given the API user makes a GET request to '/latest/USD'
    Then he looks for the API response should match the JSON schema
    And the API user should not complain with an APIError

