package com.banking.api;

import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Banking API")
@Feature("Accounts")
public class AccountTests extends BaseTest {

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

    @Test(priority = 1, description = "Get balance with valid token")
    @Story("Get Balance - Happy Path")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a valid Bearer token returns available balance and currency")
    public void testGetBalancePositive() {
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + authToken)
        .when()
                .get("/api/v1/accounts/" + ACCOUNT_ID + "/balance")
        .then()
                .statusCode(200)
                .body("availableBalance", notNullValue())
                .body("availableBalance", greaterThanOrEqualTo(0f))
                .body("currency", anyOf(equalTo("INR"), equalTo("USD"), notNullValue()));
    }

    @Test(priority = 2, description = "Get balance with invalid token should return 401")
    @Story("Get Balance - Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that an invalid token is rejected with 401")
    public void testGetBalanceInvalidToken() {
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer invalidtoken12345")
        .when()
                .get("/api/v1/accounts/" + ACCOUNT_ID + "/balance")
        .then()
                .statusCode(401);
    }

    @Test(priority = 3, description = "Get balance without Authorization header should return 401")
    @Story("Get Balance - Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that missing Authorization header results in 401")
    public void testGetBalanceNoAuthHeader() {
        given()
                .spec(requestSpec)
        .when()
                .get("/api/v1/accounts/" + ACCOUNT_ID + "/balance")
        .then()
                .statusCode(401);
    }
}
