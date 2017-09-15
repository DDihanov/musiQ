package com.dihanov.musiq.service;

import com.dihanov.musiq.models.artist.Artist;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.dihanov.musiq.config.Config.LAST_FM_API_URL;
import static com.dihanov.musiq.config.Config.LAST_FM_CALL;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface LastFmApiService{
    final String METHOD_CALL = LAST_FM_API_URL + "?method=";

    @GET(METHOD_CALL+"artist.getinfo&artist={artistName}"+LAST_FM_CALL)
    Observable<Artist> getArtistInfo(@Query("artistName") String artistName);
}
