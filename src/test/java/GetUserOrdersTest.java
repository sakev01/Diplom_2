import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class GetUserOrdersTest{
    String bearerToken;
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
    @DisplayName("Check User Orders")
    @Description("Test display user orders with login")
    public void testWithLoginGetUserOrders(){
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String password = faker.internet().password();
        String email = faker.internet().emailAddress();
        UserData.createUser(email, password, name);
        bearerToken = UserData.loginUser(email, password)
                .then()
                .extract()
                .path("accessToken");
        OrderData.getUserOrders(bearerToken)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    @DisplayName("Check User Orders without authorisation")
    @Description("Test user orders without login are not displayed")
    public void testWithoutLoginGetUserOrders(){
        OrderData.getUserOrders(bearerToken)
                .then()
                .assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

}