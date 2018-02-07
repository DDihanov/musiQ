package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimitar.dihanov on 2/7/2018.
 */

public class Scrobbles {
    @SerializedName("@attr")
    @Expose
    private Attr attr;
    @SerializedName("scrobble")
    @Expose
    private Scrobble scrobble;

    public Attr getAttr() {
        return attr;
    }

    public void setAttr(Attr attr) {
        this.attr = attr;
    }

    public Scrobble getScrobble() {
        return scrobble;
    }

    public void setScrobble(Scrobble scrobble) {
        this.scrobble = scrobble;
    }

}
