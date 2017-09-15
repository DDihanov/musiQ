
package com.dihanov.musiq.models.artist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Similar {

    @SerializedName("artist")
    @Expose
    private List<Artist> artist = null;

    public List<Artist> getArtist() {
        return artist;
    }

    public void setArtist(List<Artist> artist) {
        this.artist = artist;
    }

}
