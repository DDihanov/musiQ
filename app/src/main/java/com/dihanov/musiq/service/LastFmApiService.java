package com.dihanov.musiq.service;


import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.models.TopArtistsResult;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface LastFmApiService{
    String METHOD_CALL = "?method=";

    @GET(METHOD_CALL+"artist.getinfo")
    Single<SpecificArtist> getSpecificArtistInfo(@Query("artist") String artistName);

    @GET(METHOD_CALL+"artist.search")
    Observable<ArtistSearchResults> searchForArtist(@Query("artist") String artistName, @Query("limit") Integer limit);

    @GET(METHOD_CALL+"chart.getTopArtists")
    Observable<TopArtistsResult> chartTopArtists(@Query("limit") Integer limit);


//    @GET(METHOD_CALL+"artist.getinfo")
//    Call<Artist> getArtistInfoCall(@Query("artist") String artistName);
}
