package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryService;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;


public class GenerateCategoryExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create(GenerateCategoryExtension.class);

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .build();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final CategoryService categoryService = retrofit.create(CategoryService.class);

    private String getUniqueTestId(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestClass().getSimpleName() + ":"
                + extensionContext.getRequiredTestMethod().getName();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        GenerateCategory annotation = context.getRequiredTestMethod()
                .getAnnotation(GenerateCategory.class);

        if (annotation != null) {
            List<CategoryJson> getAllUserCategories = categoryService.getCategories(annotation.username())
                    .execute()
                    .body();

            if (getAllUserCategories != null && getAllUserCategories.stream().anyMatch(x -> x.getCategory().equals(annotation.category()))) {
                CategoryJson existingCategory = getAllUserCategories.stream()
                        .filter(x -> x.getCategory().equals(annotation.category()))
                        .findFirst()
                        .orElse(null);

                context.getStore(NAMESPACE).put(getUniqueTestId(context) + "category", existingCategory);
            } else {
                CategoryJson category = new CategoryJson();
                category.setUsername(annotation.username());
                category.setCategory(annotation.category());

                CategoryJson created = categoryService.addCategory(category)
                        .execute()
                        .body();

                context.getStore(NAMESPACE).put(getUniqueTestId(context) + "category", created);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(getUniqueTestId(extensionContext) + "category", CategoryJson.class);
    }
}