package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimitar.dihanov on 2/22/2018.
 */

public class UserTopTracks {

    @SerializedName("toptracks")
    @Expose
    private Toptracks toptracks;

    public Toptracks getToptracks() {
        return toptracks;
    }

    public void setToptracks(Toptracks toptracks) {
        this.toptracks = toptracks;
    }

}