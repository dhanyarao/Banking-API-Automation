package com.banking.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for all Banking API tests.
 * - Common setup (base URI, content type)
 * - Shared auth token
 * - AllureRestAssured filter attaches request/response bodies to Allure report
 */
public class BaseTest {

    protected static String authToken;
    protected static RequestSpecification requestSpec;

    protected static final String BASE_URL = System.getenv().getOrDefault("BASE_URL", "https://api.yourbank.com");
    protected static final String USERNAME = System.getenv().getOrDefault("API_USERNAME", "testuser01");
    protected static final String PASSWORD = System.getenv().getOrDefault("API_PASSWORD", "SecurePass@123");
    protected static final String ACCOUNT_ID = System.getenv().getOrDefault("ACCOUNT_ID", "1234567890");
    protected static final String FROM_ACCOUNT = System.getenv().getOrDefault("FROM_ACCOUNT", "1234567890");
    protected static final String TO_ACCOUNT = System.getenv().getOrDefault("TO_ACCOUNT", "9876543210");

    @BeforeSuite
    public void globalSetup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
    }

    @BeforeClass
    public void classSetup() {
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}
