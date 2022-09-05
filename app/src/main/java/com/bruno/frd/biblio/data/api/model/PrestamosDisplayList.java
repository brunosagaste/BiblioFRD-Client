package com.bruno.frd.biblio.data.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PrestamosDisplayList implements Serializable {

    // estados:
    public static List<String> STATES_VALUES =
            Arrays.asList("Todos", "Vencido", "Renovable", "No renovable");

    @SerializedName("bibid")
    private int mBibid;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("copyid")
    private int mCopyid;
    @SerializedName("author")
    private String mAuthor;
    @SerializedName("due_back_dt")
    private Date mDueBackDt;
    @SerializedName("days_late")
    private int mDaysLate;
    @SerializedName("renewable")
    private boolean mRenew;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("loan_begin_dt")
    private Date mLoanBeginDt;

    public PrestamosDisplayList(int bibid, Date due_back_dt, String title,
                                  String status, String author, int copyid, int days_late, boolean renewable, Date loan_begin_dt) {
        mBibid = bibid;
        mDueBackDt = due_back_dt;
        mTitle = title;
        mStatus = status;
        mAuthor = author;
        mCopyid = copyid;
        mDaysLate = days_late;
        mRenew = renewable;
        mLoanBeginDt = loan_begin_dt;
    }

    public int getBibid() {
        return mBibid;
    }

    public Date getDueBackDt() {
        return mDueBackDt;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public int getCopyid() {
        return mCopyid;
    }

    public int getDaysLate() {
        return mDaysLate;
    }

    public boolean getRenew() {
        return mRenew;
    }

    public Date getLoanBeginDt() {
        return mLoanBeginDt;
    }

    public void setBibid(int mBibid) {
        this.mBibid = mBibid;
    }

    public void setDueBackDt(Date mDueBackDt) {
        this.mDueBackDt = mDueBackDt;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public void setCopyid(int mCopyid) {
        this.mCopyid = mCopyid;
    }

    public void setDaysLate(int mDaysLate) {
        this.mDaysLate = mDaysLate;
    }

    public void setRenew(boolean mRenew) {
        this.mRenew = mRenew;
    }

    public void setLoanBeginDt(boolean mLoanBeginDt) {
        this.mRenew = mLoanBeginDt;
    }
}
