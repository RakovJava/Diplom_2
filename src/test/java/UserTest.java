import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

public class UserTest extends RestClient {
    User user;

    @Test
    @DisplayName("Создание нового уникального пользователя")
    void createNewUser() {
        user = UserGenerator.randomUser();
        Response response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    void createDoubleUser() {
        user = UserGenerator.randomUser();
        Response response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));

        Response doubleNewResponse = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
        doubleNewResponse.then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя без Email")
    void createUserWithoutEmail(){
        user = UserGenerator.randomUserWithoutEmail();
        Response response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
        response.then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя без Password")
    void createUserWithoutPassword(){
        user = UserGenerator.randomUserWithoutPassword();
        Response response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
        response.then().assertThat()
                .statusCode(403)
                .body("success",equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя без Name")
    void createUserWithoutName() {
        user = UserGenerator.randomUserWithoutName();
        Response response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
        response.then().assertThat()
                .statusCode(403)
                .body("success",equalTo(false));
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    void loginExistingUser(){
        user = UserGenerator.randomUser();
        Response response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
        //Закинули данные созданного ранее пользователя в пользователя для логина
        User userForLogin = UserGenerator.forLogin(user.getEmail(), user.getPassword());
        Response newResponse = given()
                .spec(getBaseSpec())
                .body(userForLogin)
                .when()
                .post("/api/auth/login");
        newResponse.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    void loginNotExistingUser(){
        user = UserGenerator.randomUser();
        Response response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));

        //Закинули созданного ранее пользователя в пользователя для логина с неверным логин.пасс
        User newUser = UserGenerator.forWrongLogin(user.getEmail(),user.getPassword());
        Response newResponse = given()
                .spec(getBaseSpec())
                .body(newUser)
                .when()
                .post("/api/auth/login");
        newResponse.then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo( "email or password are incorrect"));
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    void changeUserWithAutorization(){
        user = UserGenerator.randomUser();
        Response response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));

        String accessToken = response.jsonPath().getString("accessToken");
        User changedUser = UserGenerator.forChangedUser("1"+user.getEmail(), "1"+user.getPassword(), "1"+user.getName());
        Response newResponse = given()
                .spec(getBaseSpec())
                .header("authorization", accessToken)
                .body(changedUser)
                .when()
                .patch("/api/auth/user");
        newResponse.then().assertThat()
                .statusCode(200)
                .body("user.email", equalTo(changedUser.getEmail().toLowerCase()))
                .body("user.name", equalTo(changedUser.getName()));

    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    void changeUserWithoutAutorization(){
        user = UserGenerator.randomUser();
        Response response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));

        User changedUser = UserGenerator.forChangedUser("1"+user.getEmail(), "1"+user.getPassword(), "1"+user.getName());
        Response newResponse = given()
                .spec(getBaseSpec())
                .body(changedUser)
                .when()
                .patch("/api/auth/user");
        newResponse.then().assertThat()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));

    }

}
