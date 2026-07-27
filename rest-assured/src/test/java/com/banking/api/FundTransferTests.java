package com.banking.api;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Fund Transfer API tests - positive and negative scenarios.
 */
public class FundTransferTests extends BaseTest {

    @BeforeClass
    public void ensureToken() {
        if (authToken == null || authToken.isEmpty()) {
            authToken = given()
                    .contentType("application/json")
                    .body("{\"username\":\"" + USERNAME + "\",\"password\":\"" + PASSWORD + "\"}")
            .when()
                    .post("/api/v1/auth/login")
            .then()
                    .statusCode(200)
                    .extract().path("token");
        }
    }

    @Test(priority = 1, description = "Successful fund transfer")
    public void testSuccessfulTransfer() {
        String body = String.format(
                "{\"fromAccount\":\"%s\",\"toAccount\":\"%s\",\"amount\":500.00,\"currency\":\"INR\",\"remarks\":\"RestAssured automated transfer\"}",
                FROM_ACCOUNT, TO_ACCOUNT);

        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + authToken)
                .body(body)
        .when()
                .post("/api/v1/transfer")
        .then()
                .statusCode(anyOf(is(200), is(201)))
                .body(anyOf(
                        hasKey("transactionId"),
                        hasKey("referenceNumber"),
                        hasKey("status")
                ));
    }

    @Test(priority = 2, description = "Transfer with insufficient funds should return 400")
    public void testInsufficientFunds() {
        String body = String.format(
                "{\"fromAccount\":\"%s\",\"toAccount\":\"%s\",\"amount\":99999999.00,\"currency\":\"INR\",\"remarks\":\"Should fail\"}",
                FROM_ACCOUNT, TO_ACCOUNT);

        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + authToken)
                .body(body)
        .when()
                .post("/api/v1/transfer")
        .then()
                .statusCode(400)
                .body(anyOf(
                        containsString("insufficient"),
                        hasKey("message"),
                        hasKey("error")
                ));
    }

    @Test(priority = 3, description = "Transfer with missing mandatory fields should return 400")
    public void testMissingMandatoryFields() {
        // Only fromAccount, missing toAccount and amount
        String body = String.format("{\"fromAccount\":\"%s\"}", FROM_ACCOUNT);

        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + authToken)
                .body(body)
        .when()
                .post("/api/v1/transfer")
        .then()
                .statusCode(anyOf(is(400), is(422)));
    }

    @Test(priority = 4, description = "Transfer with zero amount should be rejected")
    public void testZeroAmount() {
        String body = String.format(
                "{\"fromAccount\":\"%s\",\"toAccount\":\"%s\",\"amount\":0,\"currency\":\"INR\",\"remarks\":\"Zero amount\"}",
                FROM_ACCOUNT, TO_ACCOUNT);

        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + authToken)
                .body(body)
        .when()
                .post("/api/v1/transfer")
        .then()
                .statusCode(anyOf(is(400), is(422)));
    }
}
