package com.bruno.frd.biblio.data.api.model;

import com.google.gson.annotations.SerializedName;

public class PasswordBody {

        @SerializedName("oldpw")
        private String oldpw;

        @SerializedName("newpw")
        private String newpw;

        @SerializedName("newpwconfirm")
        private String newpwconfirm;

    public PasswordBody(String oldpw, String newpw, String newpwconfirm) {
        this.oldpw = oldpw;
        this.newpw = newpw;
        this.newpwconfirm = newpwconfirm;
    }


    public String getOldpw() {
        return oldpw;
    }

    public void setOldpw(String oldpw) {
        this.oldpw = oldpw;
    }

    public String getNewpw() {
        return newpw;
    }

    public void setNewpw(String newpw) {
        this.newpw = newpw;
    }

    public String getNewpwconfirm() {
        return newpwconfirm;
    }

    public void setNewpwconfirm(String newpwconfirm) {
        this.newpwconfirm = newpwconfirm;
    }
}
