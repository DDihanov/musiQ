package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dimitar.dihanov on 2/22/2018.
 */

public class Toptracks {

    @SerializedName("track")
    @Expose
    private List<Track> track = null;
    @SerializedName("@attr")
    @Expose
    private Attr_ attr;

    public List<Track> getTrack() {
        return track;
    }

    public void setTrack(List<Track> track) {
        this.track = track;
    }

    public Attr_ getAttr() {
        return attr;
    }

    public void setAttr(Attr_ attr) {
        this.attr = attr;
    }

}
