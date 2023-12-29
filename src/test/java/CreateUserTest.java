import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CreateUserTest {
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
    @DisplayName("Check User Creation")
    @Description("Test with correct email, password and name")
    public void testForCreateUser(){
        UserData.createUser(email, password, name)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
        bearerToken = UserData.loginUser(email, password)
                .then()
                .extract()
                .path("accessToken");
    }
    @Test
    @DisplayName("Check User Creation Duplicate")
    @Description("Test user creation with the same data")
    public void testForUniqueUser(){
        UserData.createUser(email, password, name);
        UserData.createUser(email, password, name)
                .then()
                .assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(HttpStatus.SC_FORBIDDEN);
        bearerToken = UserData.loginUser(email, password)
                .then()
                .extract()
                .path("accessToken");
    }
    @Test
    @DisplayName("Check User Creation without email")
    @Description("Test user creation required fields")
    public void testForRequiredEmail(){
        UserData.createUser(null, password, name)
                .then()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

}