import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderSteps extends RestClient{
    //Запросы
    @Step("Создание заказа авторизованным пользователем")
    public Response createOrderWithAuthorization(String accessToken, Order order) {
        return given()
                .spec(getBaseSpec())
                .header("authorization", accessToken)
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderUnauthorized(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Получение списка заказов")
    public Response getUsersOrders(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("authorization", accessToken)
                .when()
                .get("/api/orders");
    }

    @Step("Получение заказов без авторизации")
    public Response getOrdersUnauthorized() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/orders");
    }

    @Step("Получение списка возможных ингредиентов")
    public GetIngredientsResponse getAllIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/ingredients")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(GetIngredientsResponse.class);
    }

    //Ответы
    @Step("Проверка ответа создания заказа")
    public void checkCreateOrderResponse(Response response) {
        response.then()
                .statusCode(200)
                .body("success",equalTo(true))
                .body("name", notNullValue())
                .body("order",notNullValue());
    }

    @Step("Проверка ответа получения списка заказов")
    public void checkGetListOrdersResponse (Response response) {
        response.then()
                .statusCode(200)
                .body("success",equalTo(true))
                .body("orders",notNullValue())
                .body("total",notNullValue())
                .body("totalToday",notNullValue());
    }

    @Step("Проверка ответа на создание заказа без авторизации")
    public void checkCreateOrderUnauthorizedResponse(Response response) {
        response.then()
                .statusCode(200)
                .body("success",equalTo(true))
                .body("name", notNullValue())
                .body("order",notNullValue());
    }

    @Step("Проверка ответа получения списка заказов без авторизации")
    public void checkWhenGetListOfOrdersUnauthorizedResponse(Response response) {
        response.then()
                .statusCode(401)
                .body("success",equalTo(false))
                .body("message",equalTo("You should be authorised"));
    }

    @Step("Проверка ответа, заказ без ингредиентов")
    public void checkResponseOfCreateOrderWithoutIngredients (Response response) {
        response.then()
                .statusCode(400)
                .body("success",equalTo(false))
                .body("message",equalTo("Ingredient ids must be provided"));
    }

    @Step("Проверка ответа, заказ с нвеерным хэшем ингредиентов")
    public void checkResponseOfCreateOrderWithIncorrectIngredients(Response response) {
        response.then()
                .statusCode(500);
    }
}
