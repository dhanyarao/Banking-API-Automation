package com.banking.api;

import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Banking API")
@Feature("Transactions")
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
    @Story("Transaction History - Happy Path")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify transaction list is returned for a valid account")
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
    }

    @Test(priority = 2, description = "Get transactions filtered by date range")
    @Story("Transaction History - Filters")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify date-range filter returns 200")
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
