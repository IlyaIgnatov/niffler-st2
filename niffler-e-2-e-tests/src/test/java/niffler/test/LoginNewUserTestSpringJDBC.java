package niffler.test;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import niffler.db.dao.NifflerUsersDAO;
import niffler.db.dao.NifflerUsersDAOJdbc;
import niffler.db.dao.NifflerUsersDAOSpringJdbc;
import niffler.db.entity.UserEntity;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.GenerateUserSpringJDBC;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;


public class LoginNewUserTestSpringJDBC extends BaseWebTest {
    NifflerUsersDAO usersDAO = new NifflerUsersDAOSpringJdbc();

    @GenerateUserSpringJDBC(
            username = "Valentin",
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

    @GenerateUserSpringJDBC(
            username = "Lena",
            password = "12345"
    )
    @Test
    void checkUpdateUser(UserEntity user){

        UserEntity updUserEntity = new UserEntity();
        updUserEntity.setId(usersDAO.getUserId(user.getUsername()));
        updUserEntity.setUsername(user.getUsername() + "-updated");
        updUserEntity.setPassword("123456");
        updUserEntity.setEnabled(false);
        updUserEntity.setAccountNonExpired(false);
        updUserEntity.setAccountNonLocked(false);
        updUserEntity.setCredentialsNonExpired(false);

        usersDAO.updateUser(updUserEntity);

        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(updUserEntity.getUsername());
        $("input[name='password']").setValue(updUserEntity.getPassword());
        $("button[type='submit']").click();

        $(byText("User account is locked")).should(visible);
    }

    @GenerateUserSpringJDBC(
            username = "Ivan",
            password = "12345"
    )
    @Test
    void checkDeleteUser(UserEntity user){
        System.out.println(usersDAO.getUserId(user.getUsername()));
        usersDAO.deleteUser(usersDAO.getUserId(user.getUsername()));

        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $(byText("Bad credentials")).should(visible);
    }

    @GenerateUserSpringJDBC(
            username = "Maxim",
            password = "12345"
    )
    @Test
    void checkReadUser(UserEntity user){

        UserEntity readUser = usersDAO.readUser(usersDAO.getUserId(user.getUsername()));
        Assertions.assertEquals(user,readUser);
    }
}
