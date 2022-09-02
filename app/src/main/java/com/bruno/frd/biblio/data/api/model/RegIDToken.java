package com.bruno.frd.biblio.data.api.model;

/**
 * Objeto plano Java para representar tokens
 */

public class RegIDToken {

    private String token;

    public RegIDToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
