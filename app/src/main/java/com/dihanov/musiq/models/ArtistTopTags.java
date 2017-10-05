
package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtistTopTags {

    @SerializedName("toptags")
    @Expose
    private Toptags toptags;

    public Toptags getToptags() {
        return toptags;
    }

    public void setToptags(Toptags toptags) {
        this.toptags = toptags;
    }

}
