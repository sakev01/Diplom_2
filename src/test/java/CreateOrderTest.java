import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.github.javafaker.Faker;
import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
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
    @DisplayName("Check Oder Creation")
    @Description("Test with login to create order")
    public void testWithLoginCreateOrder(){
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String password = faker.internet().password();
        String email = faker.internet().emailAddress();
        UserData.createUser(email, password, name);
        bearerToken = UserData.loginUser(email, password)
                .then()
                .extract()
                .path("accessToken");

        ArrayList<String> ingredients = OrderData.getIngredients(3);
        OrderData.createOrder(bearerToken, ingredients)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    @DisplayName("Check Oder Creation")
    @Description("Test without login to create order")
    public void testWithoutLoginCreateOrder(){
        ArrayList<String> ingredients = OrderData.getIngredients(3);
        OrderData.createOrder(bearerToken, ingredients)
                .then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(HttpStatus.SC_OK);
    }
    @Test
    @DisplayName("Check Oder Creation")
    @Description("Test without ingredients to create order")
    public void testWithoutIngredientsCreateOrder(){
        ArrayList<String> ingredients = new ArrayList<>();
        OrderData.createOrder(bearerToken, ingredients)
                .then()
                .assertThat()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
    @Test
    @DisplayName("Check Oder Creation")
    @Description("Test with wrong ingredients to create order")
    public void testWithWrongIngredientsCreateOrder(){
        ArrayList<String> ingredients = Constants.WRONG_INGREDIENTS;
        OrderData.createOrder(bearerToken, ingredients)
                .then()
                .assertThat()
                .body("message", equalTo("One or more ids provided are incorrect"))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

}