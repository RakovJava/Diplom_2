import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserTest extends RestClient {
    User user;
    UserSteps userSteps = new UserSteps();

    @Test
    @DisplayName("Создание нового уникального пользователя")
    void createNewUser() {
        User user = UserGenerator.randomUser();
        Response response = userSteps.createNewUser(user);
        userSteps.checkSuccessResponse(response);
        userSteps.deleteUser(userSteps.getAccessToken(response), user);
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    void createDoubleUser() {
        user = UserGenerator.randomUser();
        Response response = userSteps.createNewUser(user);
        userSteps.checkSuccessResponse(response);
        Response doubleNewResponse = userSteps.createExistUser(user);
        userSteps.checkForbiddenExistUserResponse(doubleNewResponse);
    }

    @Test
    @DisplayName("Создание пользователя без Email")
    void createUserWithoutEmail(){
        user = UserGenerator.randomUserWithoutEmail();
        Response response = userSteps.createUserWithoutEmail(user);
        userSteps.checkForbiddenWithoutRequiredResponse(response);
    }

    @Test
    @DisplayName("Создание пользователя без Password")
    void createUserWithoutPassword(){
        user = UserGenerator.randomUserWithoutPassword();
        Response response = userSteps.createUserWithoutPassword(user);
        userSteps.checkForbiddenWithoutRequiredResponse(response);
    }

    @Test
    @DisplayName("Создание пользователя без Name")
    void createUserWithoutName() {
        user = UserGenerator.randomUserWithoutName();
        Response response = userSteps.createUserWithoutName(user);
        userSteps.checkForbiddenWithoutRequiredResponse(response);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    void loginExistingUser(){
        user = UserGenerator.randomUser();
        Response response = userSteps.createNewUser(user);
        userSteps.checkSuccessResponse(response);
        Response newResponse = userSteps.loginUser(user);
        userSteps.checkSuccessLoginResponse(user, newResponse);
        userSteps.deleteUser(userSteps.getAccessToken(response), user);
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    void loginNotExistingUser(){
        user = UserGenerator.randomUser();
        Response response = userSteps.createNewUser(user);
        userSteps.checkSuccessResponse(response);
        Response newResponse = userSteps.loginIncorrectUser(user);
        userSteps.checkFailLoginResponse(newResponse);
        userSteps.deleteUser(userSteps.getAccessToken(response), user);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    void changeUserWithAuthorization(){
        user = UserGenerator.randomUser();
        Response response = userSteps.createNewUser(user);
        userSteps.checkSuccessResponse(response);
        //Меняем данные пользователя
        User changedUser = UserGenerator.forChangedUser("1"+user.getEmail(), "1"+user.getPassword(), "1"+user.getName());
        Response newResponse = userSteps.changeUserDataWithAuthorization(changedUser, response);
        userSteps.checkSuccessUpdateUserResponse(changedUser, newResponse);
        userSteps.deleteUser(userSteps.getAccessToken(response), user);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    void changeUserWithoutAuthorization(){
        user = UserGenerator.randomUser();
        Response response = userSteps.createNewUser(user);
        userSteps.checkSuccessResponse(response);
        //Меняем данные пользователя
        User changedUser = UserGenerator.forChangedUser("1"+user.getEmail(), "1"+user.getPassword(), "1"+user.getName());
        Response newResponse = userSteps.changeUserDataWithoutAuthorization(changedUser);
        userSteps.checkUnauthorizedUpdateUserResponse(newResponse);
        userSteps.deleteUser(userSteps.getAccessToken(response), user);
    }

}
