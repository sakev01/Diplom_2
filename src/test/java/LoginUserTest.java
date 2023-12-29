import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    String bearerToken;
    String name;
    String password;
    String email;
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URI;
        Faker faker = new Faker();
        this.name = faker.name().firstName();
        this.password = faker.internet().password();
        this.email = faker.internet().emailAddress();
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
        UserData.createUser(email, password, name);
        bearerToken = UserData.loginUser(email, password)
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
        UserData.createUser(email, password, name);
        UserData.loginUser("incorrect" + email, password)
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