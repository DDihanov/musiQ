package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dimitar.dihanov on 2/12/2018.
 */

public class Recenttracks {

    @SerializedName("track")
    @Expose
    private List<Track> track = null;
    @SerializedName("@attr")
    @Expose
    private Attr attr;

    public List<Track> getTrack() {
        return track;
    }

    public void setTrack(List<Track> track) {
        this.track = track;
    }

    public Attr getAttr() {
        return attr;
    }

    public void setAttr(Attr attr) {
        this.attr = attr;
    }

}
