package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface UserService {

    @POST("/updateUserInfo")
    Call<UserJson> updateUserInfo(@Body UserJson userJson);

    @GET("/currentUser")
    Call<UserJson> getCurrentUser(@Query("username") String username);
}
