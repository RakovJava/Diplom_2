import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    //Рандомный пользователь со всеми полями
    public static User randomUser() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@" + RandomStringUtils.randomAlphabetic(5) + ".ru";
        String password = RandomStringUtils.randomNumeric(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(email, password, name);
    }
    //Рандомный пользователь без mail
    public static User randomUserWithoutEmail() {
        String password = RandomStringUtils.randomNumeric(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(null, password, name);
    }
    //Рандомный пользователь без password
    public static User randomUserWithoutPassword() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@" + RandomStringUtils.randomAlphabetic(5) + ".ru";
        String name = RandomStringUtils.randomAlphabetic(8);
        return new User(email, null, name);
    }
    //Рандомный пользователь без name
    public static User randomUserWithoutName() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@" + RandomStringUtils.randomAlphabetic(5) + ".ru";
        String password = RandomStringUtils.randomNumeric(8);
        return new User(email, password, null);
    }
    //Рандомный пользователь с неверным email
    public static User randomUserWithWrongEmail() {
        String email = RandomStringUtils.randomNumeric(8);
        String password = RandomStringUtils.randomNumeric(8);
        return new User(email, password, null);
    }

    //Для логина
    public static User forLogin(String email, String password) {
        return new User(email, password, null);
    }
    //Для логина с неверным email и password
    public static User forWrongLogin(String email, String password) {
        return new User("1"+email, "2"+password, null);
    }
    //Для изменения данных пользователя
    public static User forChangedUser (String email, String password, String name) {
        return new User(email, password, name);
    }
}
