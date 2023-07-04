
package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendService;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Date;

public class GenerateSpendExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
        .create(GenerateSpendExtension.class);

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .build();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendService spendService = retrofit.create(SpendService.class);

    private String getUniqueTestId(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestClass().getSimpleName() + ":"
                + extensionContext.getRequiredTestMethod().getName();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        GenerateSpend annotation = context.getRequiredTestMethod()
            .getAnnotation(GenerateSpend.class);

        if (annotation != null) {
            SpendJson spend = new SpendJson();
            spend.setUsername(annotation.username());
            spend.setAmount(annotation.amount());
            spend.setDescription(annotation.description());
            spend.setCategory(annotation.category());
            spend.setSpendDate(new Date());
            spend.setCurrency(annotation.currency());

            SpendJson created = spendService.addSpend(spend)
                .execute()
                .body();
            context.getStore(NAMESPACE).put(getUniqueTestId(context) + "spend", created);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
        ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext,
        ExtensionContext extensionContext) throws ParameterResolutionException {

        return extensionContext.getStore(NAMESPACE).get( getUniqueTestId(extensionContext) + "spend", SpendJson.class);
    }
}
