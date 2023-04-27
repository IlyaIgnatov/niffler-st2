package niffler.test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.conditions.Text;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

import niffler.db.dao.NifflerUsersDAO;
import niffler.db.dao.NifflerUsersDAOJdbc;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import niffler.jupiter.annotation.ClasspathUser;
import niffler.jupiter.annotation.GenerateUser;
import niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginNewUserTest extends BaseWebTest {

    @GenerateUser(
            username = "Valentin3",
            password = "12345"
    )
    @Test
    void loginTest(UserEntity user) {
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
    }

    @GenerateUser(
            username = "Lena",
            password = "12345"
    )
    @Test
    void checkUpdateUser(UserEntity user){
        NifflerUsersDAO usersDAO = new NifflerUsersDAOJdbc();

        UserEntity updUserEntity = new UserEntity();
        updUserEntity.setUsername(user.getUsername() + "-updated");
        updUserEntity.setPassword("123456");
        updUserEntity.setEnabled(false);
        updUserEntity.setAccountNonExpired(false);
        updUserEntity.setAccountNonLocked(false);
        updUserEntity.setCredentialsNonExpired(false);

        usersDAO.updateUser(usersDAO.getUserId(user.getUsername()), updUserEntity);

        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(updUserEntity.getUsername());
        $("input[name='password']").setValue(updUserEntity.getPassword());
        $("button[type='submit']").click();

        $(byText("User account is locked")).should(visible);
    }

    @GenerateUser(
            username = "Lena",
            password = "12345"
    )
    @Test
    void checkDeleteUser(UserEntity user){
        NifflerUsersDAO usersDAO = new NifflerUsersDAOJdbc();

        usersDAO.deleteUser(usersDAO.getUserId(user.getUsername()));

        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $(byText("Bad credentials")).should(visible);
    }

    @GenerateUser(
            username = "Maxim",
            password = "12345"
    )
    @Test
    void checkReadUser(UserEntity user){
        NifflerUsersDAO usersDAO = new NifflerUsersDAOJdbc();

        UserEntity readedUser = usersDAO.readUser(usersDAO.getUserId(user.getUsername()));
        Assertions.assertEquals(user,readedUser);
    }
}
