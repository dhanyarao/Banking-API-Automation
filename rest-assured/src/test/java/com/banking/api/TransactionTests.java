package com.banking.api;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Transaction History API tests.
 */
public class TransactionTests extends BaseTest {

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

    @Test(priority = 1, description = "Get transaction history - positive")
    public void testGetTransactionHistory() {
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + authToken)
                .queryParam("accountId", ACCOUNT_ID)
                .queryParam("page", 1)
                .queryParam("size", 10)
        .when()
                .get("/api/v1/transactions")
        .then()
                .statusCode(200);
        // Note: Response structure varies. Add specific body assertions based on your API.
    }

    @Test(priority = 2, description = "Get transactions filtered by date range")
    public void testGetTransactionsByDateRange() {
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + authToken)
                .queryParam("accountId", ACCOUNT_ID)
                .queryParam("fromDate", "2026-07-01")
                .queryParam("toDate", "2026-07-27")
        .when()
                .get("/api/v1/transactions")
        .then()
                .statusCode(200);
    }
}
