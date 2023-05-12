package niffler.test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static niffler.jupiter.annotation.User.UserType.*;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.User;
import niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class FriendsWebTest extends BaseWebTest {

    @AllureId("102")
    @Test
    void friendsShouldBeVisible0(@User(userType = WITH_FRIENDS) UserJson user1,
                                 @User(userType = WITH_FRIENDS) UserJson user2,
                                 @User(userType = INVITATION_SENT) UserJson user3,
                                 @User(userType = INVITATION_SENT) UserJson user4,
                                 @User(userType = INVITATION_RECEIVED) UserJson user5) {
        Assertions.assertNotNull(user1);
        Assertions.assertNotNull(user2);
        Assertions.assertNotNull(user3);
        Assertions.assertNotNull(user4);
        Assertions.assertNotNull(user5);
        System.out.println("First user credentials: " + user1.getUsername() + " " + user1.getPassword());
        System.out.println("Second user credentials: " + user2.getUsername() + " " + user2.getPassword());
        System.out.println("3d user credentials: " + user3.getUsername() + " " + user3.getPassword());
        System.out.println("4d user credentials: " + user4.getUsername() + " " + user4.getPassword());
        System.out.println("5d user credentials: " + user5.getUsername() + " " + user5.getPassword());
    }

    @Disabled
    @AllureId("103")
    @Test
    void friendsShouldBeVisible1(@User(userType = WITH_FRIENDS) UserJson user) {
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='people']").click();
        $$(".table tbody tr").find(Condition.text("Pending invitation"))
                .should(Condition.visible);
    }


    @Disabled
    @AllureId("104")
    @Test
    void friendsShouldBeVisible2(@User(userType = WITH_FRIENDS) UserJson user1) {
        Assertions.assertNotNull(user1);
        System.out.println("First user credentials: " + user1.getUsername() + " " + user1.getPassword());
    }


    @AllureId("105")
    @Test
    void friendsShouldBeVisible3(@User(userType = WITH_FRIENDS) UserJson user1) {
        Assertions.assertNotNull(user1);
        System.out.println("First user credentials: " + user1.getUsername() + " " + user1.getPassword());
    }

    @AllureId("106")
    @Test
    void friendsShouldBeVisible4(@User(userType = INVITATION_SENT) UserJson user1) {
        Assertions.assertNotNull(user1);
        System.out.println("First user credentials: " + user1.getUsername() + " " + user1.getPassword());
    }
}
