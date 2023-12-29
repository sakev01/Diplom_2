import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {
    String bearerToken;
    String name;
    String password;
    String email;
    String updatedName;
    String updatedPassword;
    String updatedEmail;
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;
        Faker faker = new Faker();
        this.name = faker.name().firstName();
        this.password = faker.internet().password();
        this.email = faker.internet().emailAddress();
        this.updatedName = faker.name().firstName();
        this.updatedPassword = faker.internet().password();
        this.updatedEmail = faker.internet().emailAddress();
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
        UserData.createUser(email, password, name);
        bearerToken = UserData.loginUser(email, password)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("accessToken");
        UserData.updateUser(bearerToken, updatedEmail, updatedPassword, updatedName)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
        bearerToken = UserData.loginUser(updatedEmail, updatedPassword)
                .then()
                .extract()
                .path("accessToken");
    }
    @Test
    @DisplayName("Check logout")
    public void testForUpdateLogoutUser(){
        UserData.createUser(email, password, name);
        UserData.updateUser(bearerToken, updatedEmail, updatedPassword, updatedName)
                .then()
                .assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
        bearerToken = UserData.loginUser(email, password)
                .then()
                .extract()
                .path("accessToken");
    }
}
