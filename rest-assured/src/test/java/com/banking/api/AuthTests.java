package com.banking.api;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Authentication related API tests with Allure annotations.
 */
@Epic("Banking API")
@Feature("Authentication")
public class AuthTests extends BaseTest {

    @Test(priority = 1, description = "Valid login should return 200 and a JWT token")
    @Story("Login - Happy Path")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that valid credentials return HTTP 200 and a non-empty JWT token")
    public void testValidLogin() {
        authToken = given()
                .spec(requestSpec)
                .body("{\"username\":\"" + USERNAME + "\",\"password\":\"" + PASSWORD + "\"}")
        .when()
                .post("/api/v1/auth/login")
        .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("token", instanceOf(String.class))
                .extract()
                .path("token");

        Allure.step("Token extracted successfully (length: " + authToken.length() + ")");
        System.out.println("Token extracted successfully (length: " + authToken.length() + ")");
    }

    @Test(priority = 2, description = "Invalid password should return 401")
    @Story("Login - Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that wrong password returns 401 and no token is issued")
    public void testInvalidPassword() {
        given()
                .spec(requestSpec)
                .body("{\"username\":\"" + USERNAME + "\",\"password\":\"WrongPass999\"}")
        .when()
                .post("/api/v1/auth/login")
        .then()
                .statusCode(401)
                .body("token", nullValue());
    }

    @Test(priority = 3, description = "Missing credentials should return 400")
    @Story("Login - Negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that empty body is rejected with 4xx status")
    public void testMissingCredentials() {
        given()
                .spec(requestSpec)
                .body("{}")
        .when()
                .post("/api/v1/auth/login")
        .then()
                .statusCode(anyOf(is(400), is(401), is(422)));
    }
}
