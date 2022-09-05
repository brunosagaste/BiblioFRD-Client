package com.bruno.frd.biblio.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SearchDisplayList implements Serializable {

    // estados:
    public static List<String> STATES_VALUES =
            Arrays.asList("Todos", "Vencido", "Renovable", "No renovable");

    @SerializedName("bibid")
    private int mBibid;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("copy_free")
    private int mCopyFree;
    @SerializedName("author")
    private String mAuthor;
    @SerializedName("topic2")
    private String mTopic2;
    @SerializedName("due_back_dt")
    private Date mDueBackDt;


    public SearchDisplayList(int bibid, String title,
                             String status, String author, int copy_free, String topic2, Date due_back_dt) {
        mBibid = bibid;
        mTitle = title;
        mAuthor = author;
        mCopyFree = copy_free;
        mTopic2 = topic2;
        mDueBackDt = due_back_dt;
    }

    public int getBibid() {
        return mBibid;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTopic2() {
        return mTopic2;
    }

    public int getCopyFree() {
        return mCopyFree;
    }

    public Date getDueBackDt() {
        return mDueBackDt;
    }

    public void setBibid(int mBibid) {
        this.mBibid = mBibid;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public void setCopyFree(int mCopyid) {
        this.mCopyFree = mCopyid;
    }

    public void setTopic2(String mTopic2) {
        this.mTopic2 = mTopic2;
    }

    public void setDueBackDt(String mTopic2) {
        this.mDueBackDt = mDueBackDt;
    }

}


