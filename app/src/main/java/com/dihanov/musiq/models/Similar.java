
package com.dihanov.musiq.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Similar {

    @SerializedName("artist")
    @Expose
    private List<Artist_> artist = null;

    public List<Artist_> getArtist() {
        return artist;
    }

    public void setArtist(List<Artist_> artist) {
        this.artist = artist;
    }

}
