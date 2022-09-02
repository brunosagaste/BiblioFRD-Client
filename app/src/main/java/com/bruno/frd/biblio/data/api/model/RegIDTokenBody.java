package com.bruno.frd.biblio.data.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Objeto plano Java para representar el cuerpo de la petición POST /public/login
 */
public class RegIDTokenBody {
    @SerializedName("regid")
    private String regid;

    public RegIDTokenBody(String regid) {
        this.regid = regid;
    }

    public String getRegid() {
        return regid;
    }

    public void setRegid(String userId) {
        this.regid = regid;
    }

}
