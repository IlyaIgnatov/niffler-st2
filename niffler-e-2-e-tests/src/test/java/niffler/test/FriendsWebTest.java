package niffler.test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static niffler.jupiter.annotations.User.UserType.*;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotations.User;
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
        System.out.println("Test102. First user credentials: " + user1.getUsername() + " " + user1.getPassword());
        System.out.println("Test102. Second user credentials: " + user2.getUsername() + " " + user2.getPassword());
        System.out.println("Test102. 3d user credentials: " + user3.getUsername() + " " + user3.getPassword());
        System.out.println("Test102. 4d user credentials: " + user4.getUsername() + " " + user4.getPassword());
        System.out.println("Test102. 5d user credentials: " + user5.getUsername() + " " + user5.getPassword());
    }

    @AllureId("103")
    @Test
    void friendsShouldBeVisible1(@User(userType = WITH_FRIENDS) UserJson user1) {
        Assertions.assertNotNull(user1);
        System.out.println("Test104. First user credentials: " + user1.getUsername() + " " + user1.getPassword());
    }

    @AllureId("104")
    @Test
    void friendsShouldBeVisible2(@User(userType = INVITATION_SENT) UserJson user1) {
        Assertions.assertNotNull(user1);
        System.out.println("Test104. First user credentials: " + user1.getUsername() + " " + user1.getPassword());
    }

    @AllureId("105")
    @Test
    void friendsShouldBeVisible3(@User(userType = INVITATION_SENT) UserJson user1) {
        Assertions.assertNotNull(user1);
        System.out.println("Test105. First user credentials: " + user1.getUsername() + " " + user1.getPassword());
    }

    @AllureId("106")
    @Test
    void friendsShouldBeVisible4(@User(userType = INVITATION_SENT) UserJson user1) {
        Assertions.assertNotNull(user1);
        System.out.println("Test106. First user credentials: " + user1.getUsername() + " " + user1.getPassword());
    }
}
