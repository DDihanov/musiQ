package com.dihanov.musiq.service;


import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.models.ArtistTopTags;
import com.dihanov.musiq.models.ArtistTopTracks;
import com.dihanov.musiq.models.GeneralAlbumSearch;
import com.dihanov.musiq.models.LovedTracks;
import com.dihanov.musiq.models.RecentTracksWrapper;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.models.SpecificAlbum;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.models.TopArtistAlbums;
import com.dihanov.musiq.models.TopArtistsResult;
import com.dihanov.musiq.models.User;
import com.dihanov.musiq.models.UserArtistTracks;
import com.dihanov.musiq.models.UserTopArtists;
import com.dihanov.musiq.models.UserTopTracks;

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

    @GET(METHOD_CALL+"user.getlovedtracks")
    Observable<LovedTracks> getUserLovedTracks(@Query("user") String username, @Query("limit") String limit);

    @GET(METHOD_CALL+"artist.gettoptracks")
    Observable<ArtistTopTracks> getArtistTopTracks(@Query("artist") String artist, @Query("limit") int limit);

    @GET(METHOD_CALL+"user.getrecenttracks")
    Observable<RecentTracksWrapper> getUserRecentTracks(@Query("user") String username, @Query("limit") int limit, @Query("extended") int extended);

    @GET(METHOD_CALL+"user.getartisttracks")
    Observable<UserArtistTracks> getUserArtistTracks(@Query("user") String user, @Query("limit") int limit, @Query("artist") String artist);

    @GET(METHOD_CALL+"user.gettoptracks")
    Observable<UserTopTracks> getUserTopTracks(@Query("user") String user, @Query("limit") int limit, @Query("period") String period);

    @GET(METHOD_CALL+"user.gettopartists")
    Observable<UserTopArtists> getUserTopArtists(@Query("user") String user, @Query("limit") int limit, @Query("period") String period);

    @FormUrlEncoded
    @POST(Config.LAST_FM_API_URL)
    Observable<User> getMobileSessionToken(@Field("method") String authMethod, @Field("username") String username, @Field("password") String password, @Field("api_key") String apiKey, @Field("api_sig") String apiSig, @Field("format") String format);

    @FormUrlEncoded
    @POST(Config.LAST_FM_API_URL)
    Observable<Response> scrobbleTrack(@Field("method") String scrobbleMethod, @Field("artist") String artist, @Field("track") String track, @Field("api_key") String apiKey, @Field("api_sig") String apiSig, @Field("timestamp") long timestamp, @Field("sk") String sessionKey,@Field("format") String format);

    @FormUrlEncoded
    @POST(Config.LAST_FM_API_URL)
    Observable<Response> scrobbleTrack(@Field("method") String scrobbleMethod, @Field("artist") String artist, @Field("track") String track, @Field("api_key") String apiKey, @Field("api_sig") String apiSig, @Field("timestamp") String timestamp, @Field("sk") String sessionKey,@Field("format") String format);

    @FormUrlEncoded
    @POST(Config.LAST_FM_API_URL)
    Observable<Response> updateNowPlaying(@Field("method") String updateNowPlayingMethod, @Field("artist") String artist, @Field("track") String track, @Field("api_key") String apiKey, @Field("api_sig") String apiSig, @Field("sk") String sessionKey,@Field("format") String format);

    @FormUrlEncoded
    @POST(Config.LAST_FM_API_URL)
    Observable<Response> loveTrack(@Field("method") String trackLoveMethod, @Field("artist") String artist, @Field("track") String track, @Field("api_key") String apiKey, @Field("api_sig") String apiSig, @Field("sk") String sessionKey,@Field("format") String format);
}
