
package com.dihanov.musiq.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("session")
    @Expose
    private Session session;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("realname")
    @Expose
    private String realname;
    @SerializedName("image")
    @Expose
    private List<Image> image = null;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("subscriber")
    @Expose
    private String subscriber;
    @SerializedName("playcount")
    @Expose
    private String playcount;
    @SerializedName("playlists")
    @Expose
    private String playlists;
    @SerializedName("bootstrap")
    @Expose
    private String bootstrap;
    @SerializedName("registered")
    @Expose
    private Registered registered;
    @SerializedName("type")
    @Expose
    private String type;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public String getPlaylists() {
        return playlists;
    }

    public void setPlaylists(String playlists) {
        this.playlists = playlists;
    }

    public String getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(String bootstrap) {
        this.bootstrap = bootstrap;
    }

    public Registered getRegistered() {
        return registered;
    }

    public void setRegistered(Registered registered) {
        this.registered = registered;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
