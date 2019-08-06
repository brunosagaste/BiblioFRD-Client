package com.hermosaprogramacion.blog.saludmock.data.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Objeto plano Java para representar el cuerpo de la petición POST /affiliates/login
 */
public class LoginBody {
    @SerializedName("email")
    private String email;
    private String password;

    public LoginBody(String userId, String password) {
        this.email = userId;
        this.password = password;
    }

    public String getUserId() {
        return email;
    }

    public void setUserId(String userId) {
        this.email = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
