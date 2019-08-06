package com.hermosaprogramacion.blog.saludmock.data.api.model;

/**
 * Objeto plano Java para representar afiliados
 */
public class Affiliate {

    private String id;
    private String name;
    private String email;
    private String gender;
    private String token;

    public Affiliate(String id, String name, String email, String gender, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String address) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
