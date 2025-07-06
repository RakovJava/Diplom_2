import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserUpdateTest extends RestClient {
    User user;
    UserSteps userSteps = new UserSteps();

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
