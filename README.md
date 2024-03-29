**Exchange Rate API Testing with Serenity BDD & Screenplay**

This project demonstrates automated API testing for an exchange rate service using Serenity BDD and Screenplay frameworks with Maven.

**Features:**
Verify exchange rate retrieval for a given currency pair.
Ensure response time meets expected performance benchmarks.

**Project Structure:**
src/main/java: Contains Java source code for step definitions and Serenity Screenplay implementations.
src/test/java: Houses the feature files defining test steps & runner.
src/test/resources/features: Houses the feature files defining test scenarios.
pom.xml: Defines project dependencies and build configuration.

**Dependencies:**
Serenity BDD
Serenity REST
Serenity Screenplay
(Optional) Additional libraries based on your specific API interactions (e.g., JSON parsing)

**Getting Started:**
Clone the project: Replace "REPOSITORY_URL" with the actual URL for your project repository.

bash
Copy code
git clone REPOSITORY_URL
Set up dependencies: Ensure you have Maven installed and run the following command to download dependencies:

bash
Copy code
mvn clean install
Run Tests: Execute the tests with the following command:

bash
Copy code
mvn verify


**Testing Exchange Rates:**
The provided feature files (details not included due to potential sensitive information) showcase scenarios for testing exchange rate retrieval functionalities.

**Response Time Verification:**
The project includes logic to measure API response times and asserts they meet performance expectations. Customizations can be made to these assertions based on requirements.

**Serenity Screenplay Implementation:**
Serenity Screenplay actors are used to interact with the API and perform assertions on the responses. Tasks and interactions handle API calls and verifications in a concise and readable manner.

**Deserialization:**
The project utilizes deserialization to convert API responses into Java objects, allowing for easier handling and assertion of response data.

**Developing Your Tests:**
This project provides a foundation for building further API testing scenarios for your exchange rate service. Extend the feature files and step definitions to cover additional functionalities and edge cases.

**Additional Notes:**
Modify the project structure and dependencies as needed for your specific testing environment and API interactions.
Refer to the Serenity BDD and Screenplay documentation for detailed information on these frameworks and best practices.