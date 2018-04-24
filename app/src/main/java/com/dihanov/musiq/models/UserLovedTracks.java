package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimitar.dihanov on 2/8/2018.
 */

public class UserLovedTracks {
    @SerializedName("lovedtracks")
    @Expose
    private Lovedtracks lovedtracks;

    public Lovedtracks getLovedtracks() {
        return lovedtracks;
    }

    public void setLovedtracks(Lovedtracks lovedtracks) {
        this.lovedtracks = lovedtracks;
    }
}
