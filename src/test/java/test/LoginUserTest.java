package test;

import api.UserData;
import com.github.javafaker.Faker;
import constants.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.NewUser;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.TestHelper;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    String bearerToken;
    String name;
    String password;
    String email;
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;

    }
    @After
    public void deleteUser() {
        if(bearerToken != null) {
            UserData.deleteUser(bearerToken);
        }
    }
    @Test
    @DisplayName("Check login")
    @Description("Create a new user and login with the same user")
    public void testForLoginUser(){
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
    }
    @Test
    @DisplayName("Check login with wrong  email")
    @Description("Create a new user and login with incorrect email")
    public void testForLoginUserWithIncorrectEmail(){
        NewUser testUser = TestHelper.createTestUser();
        UserData.createUser(testUser.getEmail(), testUser.getPassword(), testUser.getName());

        UserData.loginUser("incorrect" + testUser.getEmail(), testUser.getPassword())
                .then()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

        bearerToken = UserData.loginUser(email, password)
                .then()
                .extract()
                .path("accessToken");
    }

}