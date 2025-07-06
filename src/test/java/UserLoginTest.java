import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserLoginTest extends RestClient {
    User user;
    UserSteps userSteps = new UserSteps();

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
}
