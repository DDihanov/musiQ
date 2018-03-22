package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimitar.dihanov on 3/22/2018.
 */

public class Registered {

    @SerializedName("#text")
    @Expose
    private Integer text;
    @SerializedName("unixtime")
    @Expose
    private String unixtime;

    public Integer getText() {
        return text;
    }

    public void setText(Integer text) {
        this.text = text;
    }

    public String getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(String unixtime) {
        this.unixtime = unixtime;
    }

}
