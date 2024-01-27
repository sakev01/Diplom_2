package util;

import com.github.javafaker.Faker;
import model.NewUser;

public class TestHelper {

    /**
     * Creates a test user using Faker and UserData class.
     * It generates random user details and registers the user.
     *
     * @return NewUser object representing the created test user.
     */
//    public static NewUser createTestUser() {
//        Faker faker = new Faker();
//        String name = faker.name().firstName();
//        String password = faker.internet().password();
//        String email = faker.internet().emailAddress();
//
//        // Assuming UserData.createUser() method registers the user and doesn't return a response.
//        UserData.createUser(email, password, name);
//        return new NewUser(email, password, name);
//    }
    public static NewUser createTestUser() {
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String password = faker.internet().password();
        String email = faker.internet().emailAddress();
        return new NewUser(email, password, name);
    }
}
