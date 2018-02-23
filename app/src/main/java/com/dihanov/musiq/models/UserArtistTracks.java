package com.dihanov.musiq.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by dimitar.dihanov on 2/22/2018.
 */



public class UserArtistTracks {

    @SerializedName("artisttracks")
    @Expose
    private Artisttracks artisttracks;

    public Artisttracks getArtisttracks() {
        return artisttracks;
    }

    public void setArtisttracks(Artisttracks artisttracks) {
        this.artisttracks = artisttracks;
    }

}