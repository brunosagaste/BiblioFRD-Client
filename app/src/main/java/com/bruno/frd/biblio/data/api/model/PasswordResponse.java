package com.bruno.frd.biblio.data.api.model;

import com.google.gson.annotations.SerializedName;

public class PasswordResponse {

    @SerializedName("message")
    private String message;
    @SerializedName("developerMessage")
    private String developerMessage;
    @SerializedName("token")
    private String token;

    public PasswordResponse(String message, String developerMessage, String token) {
        this.message = message;
        this.developerMessage = developerMessage;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
