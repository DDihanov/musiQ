package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimitar.dihanov on 2/7/2018.
 */

public class Scrobble {
    @SerializedName("artist")
    @Expose
    private Artist artist;
    @SerializedName("ignoredMessage")
    @Expose
    private IgnoredMessage ignoredMessage;
    @SerializedName("albumArtist")
    @Expose
    private AlbumArtist albumArtist;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("album")
    @Expose
    private Album album;
    @SerializedName("track")
    @Expose
    private Track track;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public IgnoredMessage getIgnoredMessage() {
        return ignoredMessage;
    }

    public void setIgnoredMessage(IgnoredMessage ignoredMessage) {
        this.ignoredMessage = ignoredMessage;
    }

    public AlbumArtist getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(AlbumArtist albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}

