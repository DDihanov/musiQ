package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimitar.dihanov on 3/22/2018.
 */

public class Registered {
    @SerializedName("#text")
    @Expose
    private String text;
    @SerializedName("unixtime")
    @Expose
    private String unixtime;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(String unixtime) {
        this.unixtime = unixtime;
    }

}
