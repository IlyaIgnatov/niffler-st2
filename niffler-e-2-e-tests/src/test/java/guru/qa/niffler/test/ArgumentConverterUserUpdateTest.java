package guru.qa.niffler.test;


import guru.qa.niffler.api.UserService;
import guru.qa.niffler.jupiter.annotation.ClasspathUser;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgumentConverterUserUpdateTest {

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .build();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8089")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UserService userService = retrofit.create(UserService.class);

    @ValueSource(strings = {
            "testData/alex.json",
            "testData/semen.json"
    })
    @AllureId("105")
    @ParameterizedTest
    void userDataShouldBeUpdated(@ClasspathUser UserJson user) throws IOException {
        userService.updateUserInfo(user)
                .execute();

        UserJson userAfterUpdate = userService.getCurrentUser(user.getUsername())
                .execute()
                .body();
        assert userAfterUpdate != null;

        Assertions.assertAll(
                () -> assertEquals(user.getUsername(), userAfterUpdate.getUsername()),
                () -> assertEquals(user.getFirstname(), userAfterUpdate.getFirstname()),
                () -> assertEquals(user.getSurname(), userAfterUpdate.getSurname()),
                () -> assertEquals(user.getCurrency(), userAfterUpdate.getCurrency())
        );
    }
}
