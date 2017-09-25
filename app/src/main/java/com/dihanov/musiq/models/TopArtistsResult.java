package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dimitar Dihanov on 25.9.2017 г..
 */

public class TopArtistsResult {

    @SerializedName("artists")
    @Expose
    private Artistmatches artists;

    public Artistmatches getArtists() {
        return artists;
    }

    public void setArtists(Artistmatches artists) {
        this.artists = artists;
    }
}
