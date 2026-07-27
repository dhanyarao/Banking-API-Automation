package com.banking.api;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Authentication related API tests.
 * Login success + negative scenarios.
 */
public class AuthTests extends BaseTest {

    @Test(priority = 1, description = "Valid login should return 200 and a JWT token")
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

        System.out.println("Token extracted successfully (length: " + authToken.length() + ")");
    }

    @Test(priority = 2, description = "Invalid password should return 401")
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
