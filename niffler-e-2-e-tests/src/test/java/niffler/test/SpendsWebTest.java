package niffler.test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import niffler.jupiter.annotations.GenerateCategory;
import niffler.jupiter.annotations.GenerateSpend;
import niffler.model.CategoryJson;
import niffler.model.CurrencyValues;
import niffler.model.SpendJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SpendsWebTest extends BaseTest{

    final static String USERNAME = "PETR";
    final static String PASSWORD = "12345";
    final static String CATEGORY = "Education5";

    static {
        Configuration.browserSize = "1920x1080";
    }

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
