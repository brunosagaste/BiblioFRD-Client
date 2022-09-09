package  com.bruno.frd.biblio.data.api;

import com.bruno.frd.biblio.data.api.model.ApiMessageResponse;
import com.bruno.frd.biblio.data.api.model.ApiResponsePrestamos;
import com.bruno.frd.biblio.data.api.model.ApiSearchResponse;
import com.bruno.frd.biblio.data.api.model.LoginBody;
import com.bruno.frd.biblio.data.api.model.PasswordBody;
import com.bruno.frd.biblio.data.api.model.PasswordResponse;
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
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * REST service de BiblioFRD
 */

public interface BiblioApi {

    public static final String BASE_URL = "http://asus.lan/biblioFRD-Server/public/";

    @POST("login")
    Call<User> login(@Body LoginBody loginBody);

    @GET("copy/get")
    Call<ApiResponsePrestamos> getPrestamos(@Header("Auth") String token,
                                            @Header("Id") String id,
                                            @QueryMap Map<String, Object> parameters);

    @Headers("Content-Type: application/json")
    @GET("renewal/book")
    Call<ApiMessageResponse> renewBook(@Query("bibid") int bibId,
                                       @Query("copyid") int copyId,
                                       @Header("Auth") String token,
                                       @Header("Id") String id);

    @GET("search/text/{search_text}")
    Call<ApiSearchResponse> getSearch(@Path("search_text") String searchText,
                                      @Header("Auth") String token,
                                      @Header("Id") String id);

    @POST("regid")
    Call<ApiMessageResponse> sendToken(@Header("Auth") String usertoken,
                                       @Header("Id") String id,
                                       @Body RegIDTokenBody regidtoken);

    @POST("password")
    Call<PasswordResponse> sendPassword(@Header("Auth") String token,
                                        @Header("Id") String id,
                                        @Body PasswordBody password);

}
