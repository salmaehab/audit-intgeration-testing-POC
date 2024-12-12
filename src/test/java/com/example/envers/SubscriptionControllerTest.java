package com.example.envers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SubscriptionControllerTest {

    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @Value("${local.server.port}")
    private int port;

    @BeforeAll
    static void startContainer() {

        mysqlContainer.start();

        System.setProperty("DB_URL", mysqlContainer.getJdbcUrl());
        System.setProperty("DB_USERNAME", mysqlContainer.getUsername());
        System.setProperty("DB_PASSWORD", mysqlContainer.getPassword());
    }

    @BeforeAll
    static void setUpRestAssured() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void testCreateAndGetSubscriptions() {
        // Set the port dynamically
        RestAssured.port = port;

        // Create a new subscription
        String subscriptionPayload = """
                {
                    "name": "Basic Plan",
                    "description": "Monthly subscription plan"
                }
                """;

        // POST: Create Subscription
        given()
                .contentType(ContentType.JSON)
                .body(subscriptionPayload)
                .when()
                .post("/subscriptions")
                .then()
                .statusCode(200)
                .body("name", equalTo("Basic Plan"))
                .body("description", equalTo("Monthly subscription plan"));

        // GET: Retrieve All Subscriptions
        when()
                .get("/subscriptions")
                .then()
                .statusCode(200)
                .log().body()
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void testUpdateSubscription() {
        // Set the port dynamically
        RestAssured.port = port;

        String subscriptionPayload = """
                {
                    "name": "Basic Plan",
                    "description": "Monthly subscription plan"
                }
                """;

        // POST: Create a subscription
        int subscriptionId = given()
                .contentType(ContentType.JSON)
                .body(subscriptionPayload)
                .when()
                .post("/subscriptions")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        // PUT: Update the subscription
        String updatedPayload = """
                {
                    "name": "Premium Plan",
                    "description": "Yearly subscription plan"
                }
                """;
        System.out.println("subsss :{}"+subscriptionId);
        given()
                .contentType(ContentType.JSON)
                .body(updatedPayload)
                .when()
                .put("/subscriptions/" + subscriptionId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Premium Plan"))
                .body("description", equalTo("Yearly subscription plan"));
    }

    @Test
    void testDeleteSubscription() {
        // Set the port dynamically
        RestAssured.port = port;

        String subscriptionPayload = """
                {
                    "name": "Basic Plan",
                    "description": "Monthly subscription plan"
                }
                """;

        // POST: Create a subscription
        int subscriptionId = given()
                .contentType(ContentType.JSON)
                .body(subscriptionPayload)
                .when()
                .post("/subscriptions")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        // DELETE: Remove the subscription
        when()
                .delete("/subscriptions/" + subscriptionId)
                .then()
                .statusCode(204);

        // GET: Verify the subscription is deleted
        when()
                .get("/subscriptions/" + subscriptionId)
                .then()
                .statusCode(404);
    }
}
