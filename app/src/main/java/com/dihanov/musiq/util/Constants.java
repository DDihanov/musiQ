package com.dihanov.musiq.util;

import com.dihanov.musiq.BuildConfig;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class Constants {
    public static final String APP_VESRION = BuildConfig.VERSION_NAME;

    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String TRACK = "track";
    public static final String TIMESTAMP = "timestamp";
    public static final String METHOD = "method";
    public static final String LAST_SEARCH = "lastSearch";
    public static final int ALBUM_LIMIT = 20;
    public static final int IMAGE_XLARGE = 4;
    public static final int IMAGE_LARGE = 3;
    public static final String USER_SESSION_KEY = "user_session";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FAVORITE_ARTISTS_KEY = "favorite_artists";
    public static final String FAVORITE_ALBUMS_KEY = "favorite_albums";
    public static final String AUTH_MOBILE_SESSION_METHOD = "auth.getMobileSession";
    public static final String TRACK_SCROBBLE_METHOD = "track.scrobble";
    public static final String UPDATE_NOW_PLAYING_METHOD = "track.updateNowPlaying";
    public static final String LOVE_TRACK_METHOD = "track.love";
    public static final String NO_NETWORK_CONN_FOUND = "ooops! i couldn't find an internet connection!";
    public static final long NETWORK_CHECK_DELAY = 10000;
    public static final String REMEMBER_ME = "remember me";
}
