package com.banking.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for all Banking API tests.
 * Handles common setup: base URI, content type, and shared token.
 */
public class BaseTest {

    protected static String authToken;
    protected static RequestSpecification requestSpec;

    // Change these values according to your environment
    protected static final String BASE_URL = "https://api.yourbank.com";
    protected static final String USERNAME = "testuser01";
    protected static final String PASSWORD = "SecurePass@123";
    protected static final String ACCOUNT_ID = "1234567890";
    protected static final String FROM_ACCOUNT = "1234567890";
    protected static final String TO_ACCOUNT = "9876543210";

    @BeforeSuite
    public void globalSetup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeClass
    public void classSetup() {
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }
}
