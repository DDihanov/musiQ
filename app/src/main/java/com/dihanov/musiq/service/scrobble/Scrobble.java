package com.dihanov.musiq.service.scrobble;

/**
 * Created by dimitar.dihanov on 2/7/2018.
 */

public class Scrobble {
    private String artistName;
    private String trackName;
    private String albumName;
    private long duration;
    private long timestamp;
    private String imageUrl;

    public Scrobble() {
    }

    public Scrobble(String artistName, String trackName, String albumName, long duration, long timestamp, String imageUrl) {
        this.artistName = artistName;
        this.trackName = trackName;
        this.albumName = albumName;
        this.duration = duration;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }

    public Scrobble(String artistName, String trackName, String albumName, long timestamp, String imageUrl) {
        this.artistName = artistName;
        this.trackName = trackName;
        this.albumName = albumName;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
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

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
