package test;

import api.UserData;
import com.github.javafaker.Faker;
import constants.Constants;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.NewUser;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.TestHelper;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {
    String bearerToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;
    }
    @After
    public void deleteUser() {
        if (bearerToken != null) {
            UserData.deleteUser(bearerToken);
        }
    }
    @Test
    @DisplayName("Check Updated login")
    public void testForUpdateLoginUser(){
        NewUser testUser = TestHelper.createTestUser();
        UserData.createUser(testUser.getEmail(), testUser.getPassword(), testUser.getName());
        bearerToken = UserData.loginUser(testUser.getEmail(), testUser.getPassword())
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("accessToken");

        // Generate updated user data
        NewUser updatedUser = TestHelper.createTestUser();
        UserData.updateUser(bearerToken, updatedUser.getEmail(), updatedUser.getPassword(), updatedUser.getName())
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);

        // Login with updated credentials
        bearerToken = UserData.loginUser(updatedUser.getEmail(), updatedUser.getPassword())
                .then()
                .extract()
                .path("accessToken");
    }
    @Test
    @DisplayName("Check logout")
    public void testForUpdateLogoutUser(){
        NewUser testUser = TestHelper.createTestUser();
        UserData.createUser(testUser.getEmail(), testUser.getPassword(), testUser.getName());

        // Attempt to update user without logging in
        NewUser updatedUser = TestHelper.createTestUser();
        UserData.updateUser(null, updatedUser.getEmail(), updatedUser.getPassword(), updatedUser.getName())
                .then()
                .assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}
