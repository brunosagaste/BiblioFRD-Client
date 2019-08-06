package com.hermosaprogramacion.blog.saludmock.data.api;

import com.hermosaprogramacion.blog.saludmock.data.api.model.Affiliate;
import com.hermosaprogramacion.blog.saludmock.data.api.model.LoginBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * REST service de SaludMock
 */

public interface SaludMockApi {

    // TODO: Cambiar host por "10.0.3.2" para Genymotion.
    // TODO: Cambiar host por "10.0.2.2" para AVD.
    // TODO: Cambiar host por IP de tu PC para dispositivo real.
    public static final String BASE_URL = "http://192.168.1.38/biblioteca/public/";

    @POST("login")
    Call<Affiliate> login(@Body LoginBody loginBody);

}
