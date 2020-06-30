package  com.bruno.frd.biblio.data.api;

import com.bruno.frd.biblio.data.api.model.ApiMessageResponse;
import com.bruno.frd.biblio.data.api.model.ApiResponsePrestamos;
import com.bruno.frd.biblio.data.api.model.ApiSearchResponse;
import com.bruno.frd.biblio.data.api.model.LoginBody;
import com.bruno.frd.biblio.data.api.model.PasswordBody;
import com.bruno.frd.biblio.data.api.model.RegIDTokenBody;
import com.bruno.frd.biblio.data.api.model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * REST service de BiblioFRD
 */

public interface BiblioApi {

    public static final String BASE_URL = "http://192.168.1.125/biblioteca/public/";

    @POST("login")
    Call<User> login(@Body LoginBody loginBody);

    @GET("copy/get")
    Call<ApiResponsePrestamos> getPrestamos(@Header("Authorization") String token,
                                            @QueryMap Map<String, Object> parameters);

    @Headers("Content-Type: application/json")
    @GET("renewal/book/{id}")
    Call<ApiMessageResponse> renewBook(@Path("id") int appoitmentId,
                                               @Header("Authorization") String token);

    @GET("search/text/{search_text}")
    Call<ApiSearchResponse> getSearch(@Path("search_text") String searchText,
                                      @Header("Authorization") String token);

    @POST("regid")
    Call<ApiMessageResponse> sendToken(@Header("Authorization") String usertoken, @Body RegIDTokenBody regidtoken);

    @POST("password")
    Call<ApiMessageResponse> sendPassword(@Header("Authorization") String token, @Body PasswordBody password);

}
