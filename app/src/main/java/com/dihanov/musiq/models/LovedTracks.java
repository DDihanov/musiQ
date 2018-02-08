package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimitar.dihanov on 2/8/2018.
 */

public class LovedTracks {

    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("track")
    @Expose
    private Track track;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

}
