package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimitar.dihanov on 2/8/2018.
 */

public class Date {

    @SerializedName("uts")
    @Expose
    private String uts;
    @SerializedName("#text")
    @Expose
    private String text;

    public String getUts() {
        return uts;
    }

    public void setUts(String uts) {
        this.uts = uts;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

