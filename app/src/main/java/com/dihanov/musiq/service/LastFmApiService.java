package com.dihanov.musiq.service;


import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.models.ArtistTopTags;
import com.dihanov.musiq.models.GeneralAlbumSearch;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.models.SpecificAlbum;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.models.TopArtistAlbums;
import com.dihanov.musiq.models.TopArtistsResult;
import com.dihanov.musiq.models.User;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface LastFmApiService{
    String METHOD_CALL = "?method=";

    @GET(METHOD_CALL+"artist.getinfo")
    Observable<SpecificArtist> getSpecificArtistInfo(@Query("artist") String artistName);

    @GET(METHOD_CALL+"artist.search")
    Observable<ArtistSearchResults> searchForArtist(@Query("artist") String artistName, @Query("limit") Integer limit);

    @GET(METHOD_CALL+"artist.gettopalbums")
    Observable<TopArtistAlbums> searchForArtistTopAlbums(@Query("artist") String artistName, @Query("limit") Integer limit);

    @GET(METHOD_CALL+"album.getinfo")
    Observable<SpecificAlbum> searchForSpecificAlbum(@Query("artist") String artistName, @Query("album") String albumName);

    @GET(METHOD_CALL+"album.search")
    Observable<GeneralAlbumSearch> searchForAlbum(@Query("album") String albumName, @Query("limit") Integer limit);

    @GET(METHOD_CALL+"artist.gettoptags")
    Observable<ArtistTopTags> getArtistTopTags(@Query("artist") String artistName);

    @GET(METHOD_CALL+"chart.gettopartists")
    Observable<TopArtistsResult> chartTopArtists(@Query("limit") Integer limit);

    @FormUrlEncoded
    @POST(Config.LAST_FM_API_URL)
    Observable<User> getMobileSessionToken(@Field("method") String authMethod, @Field("username") String username, @Field("password") String password, @Field("api_key") String apiKey, @Field("api_sig") String apiSig, @Field("format") String format);

    @FormUrlEncoded
    @POST(Config.LAST_FM_API_URL)
    Observable<Response> scrobbleTrack(@Field("method") String scrobbleMethod, @Field("artist") String artist, @Field("track") String track, @Field("api_key") String apiKey, @Field("api_sig") String apiSig, @Field("timestamp") long timestamp, @Field("sk") String sessionKey,@Field("format") String format);
}
