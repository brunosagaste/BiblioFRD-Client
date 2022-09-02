package com.bruno.frd.biblio.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiSearchResponse {

    @SerializedName("result")
    private List<SearchDisplayList> result;

    @SerializedName("response")
    private boolean response;

    @SerializedName("message")
    private String message;

    @SerializedName("href")
    private String href;

    @SerializedName("function")
    private String function;

    @SerializedName("filter")
    private String filter;

    public ApiSearchResponse(List<SearchDisplayList> result, boolean response, String message, String href, String function, String filter) {
        this.result = result;
        this.response = response;
        this.message = message;
        this.href = href;
        this.function = function;
        this.filter = filter;

    }

    public List<SearchDisplayList> getResults() {
        return result;
    }

    public void setResults(List<SearchDisplayList> mresult){
        this.result = mresult;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRssponse() {
        return response;
    }

    public void setRssponse(boolean rssponse) {
        this.response = rssponse;

    }
}