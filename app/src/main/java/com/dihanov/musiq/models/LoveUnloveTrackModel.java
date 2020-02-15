package com.dihanov.musiq.models;

public class LoveUnloveTrackModel {
    private String artistName;
    private String trackName;

    public LoveUnloveTrackModel(String artistName, String trackName) {
        this.artistName = artistName;
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }
}
