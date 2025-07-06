import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderTest {
    UserSteps userSteps = new UserSteps();
    OrderSteps orderSteps = new OrderSteps();
    GetIngredientsResponse ingredients;
    Order order;
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
    @DisplayName("Авторизация, создание заказа")
    public void createOrderWithAuthTest() {
        ingredients = orderSteps.getAllIngredients();
        ArrayList<String> tempOrder = new OrderGenerator().createRandomOrder(ingredients);
        order = new Order(tempOrder);
        Response response = orderSteps.createOrderWithAuthorization(accessToken, order);
        orderSteps.checkCreateOrderResponse(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthTest() {
        ingredients = orderSteps.getAllIngredients();
        ArrayList<String> tempOrder = new OrderGenerator().createRandomOrder(ingredients);
        order = new Order(tempOrder);
        Response response = orderSteps.createOrderUnauthorized(order);
        orderSteps.checkCreateOrderUnauthorizedResponse(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        ArrayList<String> tempOrder = new ArrayList<>(List.of(new String[]{}));
        order = new Order(tempOrder);
        Response response = orderSteps.createOrderWithAuthorization(accessToken, order);
        orderSteps.checkResponseOfCreateOrderWithoutIngredients(response);
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингредиентов")
    public void createOrderWithIncorrectIngredientsTest() {
        ArrayList<String> tempOrder = new ArrayList<>(List.of(new String[]{"1", "2"}));
        order = new Order(tempOrder);
        Response response = orderSteps.createOrderWithAuthorization(accessToken, order);
        orderSteps.checkResponseOfCreateOrderWithIncorrectIngredients(response);
    }
}
