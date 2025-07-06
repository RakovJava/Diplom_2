import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetOrdersTest {
    UserSteps userSteps = new UserSteps();
    OrderSteps orderSteps = new OrderSteps();
    private User user;
    private Response loginResponse;
    private String accessToken;

    @BeforeEach
    @Step("Создание пользователя")
    public void setUp() {
        this.user = UserGenerator.randomUser();
        this.loginResponse = userSteps.createNewUser(this.user);
        this.accessToken = loginResponse.then().extract().path("accessToken");
    }

    @AfterEach
    @Step("Удаление пользователя")
    public void tearDown() {
        userSteps.deleteUser(accessToken, user);
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void getUsersOrdersTest() {
        Response response = orderSteps.getUsersOrders(accessToken);
        orderSteps.checkGetListOrdersResponse(response);
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    public void getOrdersWithoutAuthTest() {
        Response response = orderSteps.getOrdersUnauthorized();
        orderSteps.checkWhenGetListOfOrdersUnauthorizedResponse(response);
    }
}
