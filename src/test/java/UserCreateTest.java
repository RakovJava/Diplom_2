import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserCreateTest extends RestClient {
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

}
