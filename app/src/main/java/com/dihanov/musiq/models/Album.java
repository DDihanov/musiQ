
package com.dihanov.musiq.models;

import com.dihanov.musiq.interfaces.Favoritable;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Type;
import java.util.List;

public class Album implements Favoritable {

    @SerializedName("#text")
    @Expose
    private String text;
    @SerializedName("corrected")
    @Expose
    private String corrected;
    @SerializedName("name")
    @Expose
    private String name;
    //this is neccecary as artist returns both string and artist object
    @SerializedName("artist")
    @Expose
    private Object artist;
    @SerializedName("mbid")
    @Expose
    private String mbid;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image")
    @Expose
    private List<Image> image = null;
    @SerializedName("listeners")
    @Expose
    private String listeners;
    //TODO: possible problem, as one API call for specific album returns a String playcount, the artists top album call returns a String
    @SerializedName("playcount")
    @Expose
    private Object playcount;
    @SerializedName("tracks")
    @Expose
    private Tracks tracks;
    @SerializedName("tags")
    @Expose
    private Tags tags;
    @SerializedName("wiki")
    @Expose
    private Wiki wiki;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setArtist(Object artist) {
        this.artist = artist;
    }

    public Object getArtist() {
        if (artist instanceof LinkedTreeMap) {
            LinkedTreeMap<String, String> weirdArtist = (LinkedTreeMap<String, String>) artist;
            Artist artist = new Artist();
            artist.setName(weirdArtist.get("name"));
            artist.setMbid(weirdArtist.get("mbid"));
            artist.setUrl(weirdArtist.get("url"));
            return artist;
        }
        return artist;
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

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }

    public String getListeners() {
        return listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public Object getPlaycount() {
        return playcount;
    }

    public void setPlaycount(Object playcount) {
        this.playcount = playcount;
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public Wiki getWiki() {
        return wiki;
    }

    public void setWiki(Wiki wiki) {
        this.wiki = wiki;
    }

    public String getCorrected() {
        return corrected;
    }

    public void setCorrected(String corrected) {
        this.corrected = corrected;
    }

    //this is necessary as API returns different types
    public static class DataStateDeserializer implements JsonDeserializer<Album> {
        @Override
        public Album deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Album album = new Gson().fromJson(json, Album.class);
            JsonObject jsonObject = json.getAsJsonObject();

            if (jsonObject.has("artist")) {
                JsonElement elem = jsonObject.get("artist");
                if (elem != null && !elem.isJsonNull()) {
                    if (elem.isJsonPrimitive()) {
                        album.setArtist(elem.getAsString());
                    } else {
                        album.setArtist(new Gson().fromJson(elem, Artist.class));
                    }
                }
            }

            if (jsonObject.has("playcount")) {
                JsonElement elem = jsonObject.get("playcount");
                if (elem != null && !elem.isJsonNull()) {
                    if (elem.isJsonPrimitive()) {
                        album.setPlaycount(elem.getAsInt());
                    } else {
                        album.setPlaycount(elem.getAsString());
                    }
                }
            }

            return album;
        }
    }


}
