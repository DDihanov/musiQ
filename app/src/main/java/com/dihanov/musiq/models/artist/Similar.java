
package com.dihanov.musiq.models.artist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Similar {

    @SerializedName("artist")
    @Expose
    private List<Artist__> artist = null;

    public List<Artist__> getArtist() {
        return artist;
    }

    public void setArtist(List<Artist__> artist) {
        this.artist = artist;
    }

}
