package com.bruno.frd.biblio.data.api.model;

import com.google.gson.annotations.SerializedName;

public class MessageResponse {

    @SerializedName("status")
    private int mStatus;
    @SerializedName("message")
    private String mMessage;

    public MessageResponse(int status, String message) {
        mStatus = status;
        mMessage = message;
    }

    public int getStatus() {
        return mStatus;
    }

    public String getMessage() {
        return mMessage;
    }
}
