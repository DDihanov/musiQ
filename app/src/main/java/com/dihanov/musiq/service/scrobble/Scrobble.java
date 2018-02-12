package com.dihanov.musiq.service.scrobble;

import android.graphics.Bitmap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dimitar.dihanov on 2/7/2018.
 */

public class Scrobble {
    private String artistName;
    private String trackName;
    private String albumName;
    private long duration;
    private long timestamp;
    private Bitmap albumArt;

    public Scrobble() {
    }

    public Scrobble(String artistName, String trackName, String albumName, long duration, long timestamp, Bitmap albumArt) {
        this.artistName = artistName;
        this.trackName = trackName;
        this.albumName = albumName;
        this.duration = duration;
        this.timestamp = timestamp;
        this.albumArt = albumArt;
        fixTrackData();
    }

    public Scrobble(String artistName, String trackName, String albumName, long timestamp, Bitmap albumArt) {
        this.artistName = artistName;
        this.trackName = trackName;
        this.albumName = albumName;
        this.timestamp = timestamp;
        this.albumArt = albumArt;
        fixTrackData();
    }


    public Scrobble(String artistName, String trackName, long timestamp) {
        this.artistName = artistName;
        this.trackName = trackName;
        this.timestamp = timestamp;
        fixTrackData();
    }

    private void fixTrackData() {
        String pattern = "^(.*?)\\s-\\s(.*?)$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(trackName);

        if(m.find()){
            this.trackName = m.group(2);
            this.artistName = m.group(1);
        }

    }

    public boolean isScrobbleValid() {
        return !getArtistName().equals("") && !getTrackName().equals("");
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

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }
}
