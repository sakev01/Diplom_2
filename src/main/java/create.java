package dummytest;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;


public class CreateCourierBeginnerTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void courierCanBeCreated() {
        //create test data
        File json = new File("src/test/resources/courier.json");
        //call method
        ValidatableResponse response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier")
                        .then();
        //check Response
        response.assertThat()
                .statusCode(201)
                .body("ok", is(true));


    }
}