import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserSteps extends RestClient {

    //Запросы
    @Step ("Создаем нового уникального пользователя")
    public Response createNewUser (User user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Создаем уже существующего пользователя")
    public Response createExistUser(User user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Создание пользователя без Email")
    public Response createUserWithoutEmail(User user){
        return createNewUser(user);
    }

    @Step("Создание пользователя без Password")
    public Response createUserWithoutPassword(User user){
        return createNewUser(user);
    }

    @Step("Создание пользователя без Name")
    public Response createUserWithoutName(User user){
        return createNewUser(user);
    }

    @Step("Логин существующего пользователя")
    public Response loginUser(User user) {
        //Закинули данные созданного ранее пользователя в пользователя для логина
        User userForLogin = UserGenerator.forLogin(user.getEmail(), user.getPassword());
        return given()
                .spec(getBaseSpec())
                .body(userForLogin)
                .when()
                .post("/api/auth/login");
    }

    @Step("Логин с неверным логин/пароль")
    public Response loginIncorrectUser(User user){
        //Закинули созданного ранее пользователя в пользователя для логина с неверным логин.пасс
        User newUser = UserGenerator.forWrongLogin(user.getEmail(),user.getPassword());
        return given()
                .spec(getBaseSpec())
                .body(newUser)
                .when()
                .post("/api/auth/login");
    }

    @Step ("Удаляем пользователя")
    public void deleteUser(String accessToken, User user) {
        given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202);
    }

    @Step("Меняем данные авторизованного пользователя")
    public Response changeUserDataWithAuthorization(User changedUser, Response response){
        return given()
                .spec(getBaseSpec())
                .header("authorization", getAccessToken(response))
                .body(changedUser)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Меняем данные не авторизованного пользователя")
    public Response changeUserDataWithoutAuthorization(User changedUser){
        return given()
                .spec(getBaseSpec())
                .body(changedUser)
                .when()
                .patch("/api/auth/user");
    }

    //Ответы
    @Step ("Проверяем успешного ответа создания клиента")
    public void checkSuccessResponse (Response response) {
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Step("Получаем токен Access token")
    public String getAccessToken (Response response){
        return response.jsonPath().getString("accessToken");
    }

    @Step("Проверка неуспешного ответа при повторном создании пользователя")
    public void checkForbiddenExistUserResponse(Response response){
        response.then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверка неуспешного ответа при отсутствии обязательного поля")
    public void checkForbiddenWithoutRequiredResponse(Response response){
        response.then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Проверка ответа при успешном логине пользователя")
    public void checkSuccessLoginResponse(User user, Response response){
        response.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()));
    }

    @Step("Проверка ответа при неуспешном логине пользователя")
    public void checkFailLoginResponse(Response response){
        response.then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo( "email or password are incorrect"));
    }

    @Step("Проверка ответа при успешном обновлении данных пользователя")
    public void checkSuccessUpdateUserResponse(User user, Response response){
        response.then().assertThat()
                .statusCode(200)
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()));
    }

    @Step("Проверка ответа при обновлении данных пользователя без авторизации")
    public void checkUnauthorizedUpdateUserResponse(Response response){
        response.then().assertThat()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }

}
