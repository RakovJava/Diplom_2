import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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
}
