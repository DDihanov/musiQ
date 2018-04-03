package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recenttrack {
    @SerializedName("artist")
    @Expose
    private Artist artist;
    @SerializedName("album")
    @Expose
    private Album album;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mbid")
    @Expose
    private String mbid;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("@attr")
    @Expose
    private Attr attr;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Attr getAttr() {
        return attr;
    }

    public void setAttr(Attr attr) {
        this.attr = attr;
    }
}