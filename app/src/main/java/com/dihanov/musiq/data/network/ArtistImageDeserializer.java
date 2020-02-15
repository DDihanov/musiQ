package com.dihanov.musiq.data.network;

import com.dihanov.musiq.BuildConfig;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.Image;
import com.dihanov.musiq.util.AppLog;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArtistImageDeserializer implements JsonDeserializer<Artist> {
    @Override
    public Artist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Artist artist = new Gson().fromJson(json, typeOfT);
        String mbid = artist.getMbid();

        if (mbid.isEmpty()) {
            return artist;
        }

        //quick and dirty way to obtain artist image from crossreferencing to MusicBrainz and then getting the url from wikipedia
        //this is due to the last.fm ban on artist images in their own api
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestBuilder;
        requestBuilder = new Request.Builder()
                .url("https://webservice.fanart.tv/v3/music/" + artist.getMbid() + "&?api_key=" + BuildConfig.API_KEY_FANART + "&format=json");
        Request request = requestBuilder.build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            JsonObject newResponse = new Gson().fromJson(response.body().string(), JsonObject.class);
            List<Image> images = artist.getImage();
            for (Image image :
                    images) {
                image.setText(newResponse.getAsJsonArray("artistthumb").get(0).getAsJsonObject().get("url").getAsString());
            }
        } catch (Exception e) {
            AppLog.log(getClass().getSimpleName(), e.getMessage());
            return artist;
        }

        return artist;
    }
}
