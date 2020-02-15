package com.dihanov.musiq.data.network;

import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.Image;
import com.dihanov.musiq.util.AppLog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
        Request request = new Request.Builder()
                .url("https://musicbrainz.org/ws/2/artist/" + artist.getMbid() + "?inc=url-rels&fmt=json")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.117 Safari/537.36")
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            JsonObject newResponse = new Gson().fromJson(response.body().string(), JsonObject.class);
            JsonArray relations = newResponse.getAsJsonArray("relations");
            for (int i = 0; i < relations.size(); i++) {
                JsonObject relation = relations.get(i).getAsJsonObject();
                String type = relation.get("type").getAsString();
                if (type.equals("image")) {
                    String url = relations.get(i).getAsJsonObject().get("url").getAsJsonObject().get("resource").getAsString();
                    if (url.startsWith("https://commons.wikimedia.org/wiki/File:")) {
                        String filename = url.substring(url.lastIndexOf('/') + 1);
                        url = "https://commons.wikimedia.org/wiki/Special:Redirect/file/" + filename;
                        List<Image> images = artist.getImage();
                        for (Image image:
                             images) {
                            image.setText(url);
                        }
                    }
                }
            }
        } catch (Exception e) {
            AppLog.log(getClass().getSimpleName(), e.getMessage());
            return artist;
        }

        return artist;
    }
}
