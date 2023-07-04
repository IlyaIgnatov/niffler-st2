package guru.qa.niffler.test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class MoreThanOneUserParameterTest extends BaseWebTest {

    @AllureId("102")
    @Test
    void friendsShouldBeVisible0(UserJson user1,
                                 UserJson user2,
                                 UserJson user3,
                                 UserJson user4,
                                 UserJson user5) {
        Assertions.assertNotNull(user1);
        Assertions.assertNotNull(user2);
        Assertions.assertNotNull(user3);
        Assertions.assertNotNull(user4);
        Assertions.assertNotNull(user5);
        System.out.println("Test102. First user credentials: " + user1.getUsername() + " " + user1.getPassword());
        System.out.println("Test102. Second user credentials: " + user2.getUsername() + " " + user2.getPassword());
        System.out.println("Test102. 3d user credentials: " + user3.getUsername() + " " + user3.getPassword());
        System.out.println("Test102. 4d user credentials: " + user4.getUsername() + " " + user4.getPassword());
        System.out.println("Test102. 5d user credentials: " + user5.getUsername() + " " + user5.getPassword());

        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user1.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
    }

    @AllureId("103")
    @Test
    void friendsShouldBeVisible1(UserJson user1) {
        Assertions.assertNotNull(user1);
        System.out.println("Test104. First user credentials: " + user1.getUsername() + " " + user1.getPassword());

        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user1.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
    }

    @AllureId("104")
    @Test
    void friendsShouldBeVisible2(UserJson user1) {
        Assertions.assertNotNull(user1);
        System.out.println("Test104. First user credentials: " + user1.getUsername() + " " + user1.getPassword());

        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user1.getPassword());
        $("button[type='submit']").click();

        $("a[href*='people']").click();
        $$(".table tbody tr").find(Condition.text("Pending invitation"))
                .should(Condition.visible);
    }

    @AllureId("105")
    @Test
    void friendsShouldBeVisible3(UserJson user1) {
        Assertions.assertNotNull(user1);
        System.out.println("Test105. First user credentials: " + user1.getUsername() + " " + user1.getPassword());

        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user1.getPassword());
        $("button[type='submit']").click();

        $("a[href*='people']").click();
        $$(".table tbody tr").find(Condition.text("Pending invitation"))
                .should(Condition.visible);
    }

    @AllureId("106")
    @Test
    void friendsShouldBeVisible4(UserJson user1) {
        Assertions.assertNotNull(user1);
        System.out.println("Test106. First user credentials: " + user1.getUsername() + " " + user1.getPassword());
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user1.getPassword());
        $("button[type='submit']").click();

        $("a[href*='people']").click();
        $$(".table tbody tr").find(Condition.text("Pending invitation"))
                .should(Condition.visible);
    }
}
