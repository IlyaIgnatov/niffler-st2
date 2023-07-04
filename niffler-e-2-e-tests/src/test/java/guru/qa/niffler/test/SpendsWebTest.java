package guru.qa.niffler.test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class SpendsWebTest extends BaseWebTest {
    final static String USERNAME = "PETR";
    final static String PASSWORD = "12345";
    final static String CATEGORY = "Education";


    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(USERNAME);
        $("input[name='password']").setValue(PASSWORD);
        $("button[type='submit']").click();
    }

    @GenerateCategory(
            username = USERNAME,
            category = CATEGORY
    )
    @GenerateSpend(
        username = USERNAME,
        description = "QA GURU ADVANCED VOL 2",
        currency = CurrencyValues.RUB,
        amount = 52000.00,
        category = CATEGORY
    )
    @AllureId("101")
    @Test
    void spendShouldBeDeletedByActionInTable(SpendJson spend, CategoryJson category) {
        Assertions.assertEquals(category.getCategory(), CATEGORY);

        $(".spendings-table tbody").$$("tr")
            .find(text(spend.getDescription()))
            .$("td")
            .scrollTo()
            .click();

        $$(".button_type_small").find(text("Delete selected"))
            .click();

        $(".spendings-table tbody")
            .$$("tr")
            .shouldHave(CollectionCondition.size(0));
    }
}
