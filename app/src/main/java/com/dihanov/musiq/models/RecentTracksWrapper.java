package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimitar.dihanov on 2/12/2018.
 */

public class RecentTracksWrapper {

    @SerializedName("recenttracks")
    @Expose
    private Recenttracks recenttracks;

    public Recenttracks getRecenttracks() {
        return recenttracks;
    }

    public void setRecenttracks(Recenttracks recenttracks) {
        this.recenttracks = recenttracks;
    }

}
