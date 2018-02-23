
package com.dihanov.musiq.models;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.List;

public class Track {
    @SerializedName("corrected")
    @Expose
    private String corrected;
    @SerializedName("#text")
    @Expose
    private String text;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("playcount")
    @Expose
    private String playcount;
    @SerializedName("loved")
    @Expose
    private String loved;
    @SerializedName("url")
    private String url;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("@attr")
    @Expose
    private Attr attr;
    @SerializedName("streamable")
    @Expose
    private Object streamable;
    @SerializedName("artist")
    @Expose
    private Artist artist;
    @SerializedName("date")
    @Expose
    private Date date;
    @SerializedName("image")
    @Expose
    private List<Image> image = null;

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLoved() {
        return loved;
    }

    public void setLoved(String loved) {
        this.loved = loved;
    }

    public String getCorrected() {
        return corrected;
    }

    public void setCorrected(String corrected) {
        this.corrected = corrected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Attr getAttr() {
        return attr;
    }

    public void setAttr(Attr attr) {
        this.attr = attr;
    }

    public Object getStreamable() {
        return streamable;
    }

    public void setStreamable(Object streamable) {
        this.streamable = streamable;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public String toString(){
        return this.artist.getText() + " - " + this.name;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    //this is necessary as API returns different types
    public static class TrackDataStateDeserializer implements JsonDeserializer<Track> {
        @Override
        public Track deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Track track = new Gson().fromJson(json, Track.class);
            JsonObject jsonObject = json.getAsJsonObject();

            if (jsonObject.has("streamable")) {
                JsonElement elem = jsonObject.get("streamable");
                if (elem != null && !elem.isJsonNull()) {
                    if (elem.isJsonPrimitive()) {
                        track.setStreamable(elem.getAsString());
                    } else {
                        track.setStreamable(new Gson().fromJson(elem, Streamable.class));
                    }
                }
            }

            return track;
        }
    }
}
