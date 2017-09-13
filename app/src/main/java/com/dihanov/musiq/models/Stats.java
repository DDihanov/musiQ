
package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stats {

    @SerializedName("listeners")
    @Expose
    private String listeners;
    @SerializedName("playcount")
    @Expose
    private String playcount;

    public String getListeners() {
        return listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

}
