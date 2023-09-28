package ru.yandex.praktikum.auth;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.*;
import ru.yandex.praktikum.auth.user.AuthUsers;
import ru.yandex.praktikum.helper.UserData;
import ru.yandex.praktikum.registrations.user.UsersRegistration;
import static ru.yandex.praktikum.helper.UserData.*;

@DisplayName("Проверка авторизации")
public class AuthenticationUserTest {
    private final AuthUsers authUsers = new AuthUsers();
    private final AssertsAuth assertsAuth = new AssertsAuth();
    private final static UsersRegistration usersRegistration = new UsersRegistration();
    private static ValidatableResponse authBaseUser;
    private Authentication auth;
    private static String userToken;

    @BeforeClass
    public static void creatBaseUser() {
        usersRegistration.userRegistration(UserData.baseUser());
    }

    @Test
    @DisplayName("Авторизация под существующим пользователем")
    @Description("Авторизация под существующим пользователем")
    public void successfulUserAuthentication() {
        auth = Authentication.fromRegistrationUser(UserData.baseUser());
        authBaseUser = authUsers.authenticationUser(auth);
        assertsAuth.successfulAuthentication(authBaseUser);
    }

    @Test
    @DisplayName("Авторизация с некорректным логином или паролем.")
    @Description("Авторизация с некорректным логином или паролем.")
    public void userAuthWithIncorrectLogin() {
        auth = Authentication.fromRegistrationUser(UserData.randomUser());
        authBaseUser = authUsers.authenticationUser(auth);
        assertsAuth.failedAuthentication(authBaseUser);
    }

    @Test
    @DisplayName("Авторизация с пустым логином.")
    @Description("Авторизация с пустым логином.")
    public void userAuthWithEmptyLogin() {
        auth = Authentication.fromRegistrationUser(userWithEmptyLogin());
        authBaseUser = authUsers.authenticationUser(auth);
        assertsAuth.failedAuthentication(authBaseUser);
    }

    @Test
    @DisplayName("Авторизация с пустым паролем.")
    @Description("Авторизация с пустым паролем.")
    public void userAuthWithEmptyPassword() {
        auth = Authentication.fromRegistrationUser(userWithEmptyPassword());
        authBaseUser = authUsers.authenticationUser(auth);
        assertsAuth.failedAuthentication(authBaseUser);
    }

    @AfterClass
    public static void deleteUser() {
        userToken = authBaseUser.extract().path("accessToken");
        usersRegistration.deleteUser(userToken);
    }

}