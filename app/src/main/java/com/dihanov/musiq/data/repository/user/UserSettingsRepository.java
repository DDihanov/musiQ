package com.dihanov.musiq.data.repository.user;

import android.content.SharedPreferences;

import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.TopArtistSource;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserSettingsRepository {
    private SharedPreferences sharedPreferences;

    @Inject
    public UserSettingsRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void registerOnSharedPrefChangedListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public boolean isAutoDetectEnabled() {
        return getSharedPreferences().getBoolean("enable_auto_detect", true);
    }

    private SharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }

    public boolean getRegisteredPlayer(String player) {
        return getSharedPreferences().getBoolean(player, true);
    }

    public void putMediaController(String controller) {
        getSharedPreferences()
                .edit()
                .putString(controller, controller)
                .apply();
    }

    public String getUserSessionKey() {
        return getSharedPreferences().getString(Constants.USER_SESSION_KEY, "");
    }


    public boolean hasSessionKey() {
        return getSharedPreferences().contains(Constants.USER_SESSION_KEY);
    }

    public boolean hasNotificationsEnabled() {
        return getSharedPreferences().getBoolean("scrobble_notifications", true);
    }

    public boolean hasScrobbleReviewEnabled() {
        return getSharedPreferences().getBoolean("scrobble_review", false);
    }


    public Set<String> getFavoriteArtists() {
        return getSharedPreferences().getStringSet(Constants.FAVORITE_ARTISTS_KEY, new HashSet<>());
    }

    public Set<String> getFavoriteAlbums() {
        return getSharedPreferences().getStringSet(Constants.FAVORITE_ALBUMS_KEY, new HashSet<>());
    }

    public String getUsername() {
        return getSharedPreferences().getString(Constants.USERNAME, "");
    }

    public void persistProfilePictureUrl(String profilePicUrl) {
        getSharedPreferences().edit().putString(Constants.PROFILE_PIC, profilePicUrl).apply();
    }

    public boolean hasRememberMeEnabled() {
        return getSharedPreferences().getBoolean(Constants.REMEMBER_ME, false);
    }

    public String getPassword() {
        return getSharedPreferences().getString(Constants.PASSWORD, "");
    }

    public void clearLoginData() {
        getSharedPreferences().edit().remove(Constants.USERNAME).remove(Constants.PASSWORD).apply();
    }

    public void persistSessionKey(String key) {
        getSharedPreferences().edit().putString(Constants.USER_SESSION_KEY, key).apply();
    }

    public void persistUsername(String username) {
        getSharedPreferences().edit().putString(Constants.USERNAME, username).apply();
    }

    public void persistPassword(String password) {
        getSharedPreferences().edit().putString(Constants.PASSWORD, password).apply();
    }

    public void setRememberMe(boolean rememberMe) {
        getSharedPreferences().edit().putBoolean(Constants.REMEMBER_ME, rememberMe).apply();
    }

    public boolean isFirstLaunch() {
        return getSharedPreferences().getBoolean(Constants.FIRST_TIME, true);
    }

    public void setIsFirstLaunch(boolean b) {
        getSharedPreferences().edit().putBoolean(Constants.FIRST_TIME, b).apply();
    }

    public int getSelectedChartToShow() {
        return getSharedPreferences().getInt(Constants.TOP_ARTIST_SOURCE, TopArtistSource.LAST_FM_CHARTS);
    }

    public Set<String> getSerializedFavoriteAlbums() {
        return getSharedPreferences().getStringSet(Constants.FAVORITE_ALBUMS_SERIALIZED_KEY, new HashSet<>());
    }

    public Set<String> getFavoriteArtistsSerialized() {
        return getSharedPreferences().getStringSet(Constants.FAVORITE_ARTISTS_SERIALIZED_KEY, new HashSet<>());
    }

    public void setChartToLastFmChart() {
        getSharedPreferences().edit().putInt(Constants.TOP_ARTIST_SOURCE, TopArtistSource.LAST_FM_CHARTS).apply();
    }

    public void setChartToUserTopArtists() {
        getSharedPreferences().edit().putInt(Constants.TOP_ARTIST_SOURCE, TopArtistSource.USER_TOP_ARTISTS_CHART).apply();
    }

    public int getChartTimeFrame() {
        return getSharedPreferences().getInt(Constants.USER_TOP_ARTIST_CHART_TIMEFRAME, 0);
    }

    public void persistSelectedChartTimeframe(int timeframe) {
        getSharedPreferences().edit().putInt(Constants.USER_TOP_ARTIST_CHART_TIMEFRAME, timeframe).apply();
    }

    public Map<String, ?> getAll() {
        return getSharedPreferences().getAll();
    }

    public boolean hasFavoriteArtists() {
        return getSharedPreferences().contains(Constants.FAVORITE_ARTISTS_KEY);
    }

    public void initFavoriteArtists() {
        getSharedPreferences().edit()
                .putStringSet(Constants.FAVORITE_ARTISTS_KEY, new HashSet<>())
                .putStringSet(Constants.FAVORITE_ARTISTS_SERIALIZED_KEY, new HashSet<>())
                .apply();
    }

    public boolean hasFavoriteAlbums() {
        return getSharedPreferences().contains(Constants.FAVORITE_ALBUMS_KEY);
    }

    public void initFavoriteAlbums() {
        getSharedPreferences().edit()
                .putStringSet(Constants.FAVORITE_ALBUMS_KEY, new HashSet<>())
                .putStringSet(Constants.FAVORITE_ALBUMS_SERIALIZED_KEY, new HashSet<>())
                .apply();
    }

    public Set<String> getFavoriteSetByKey(String key) {
        return getSharedPreferences().getStringSet(key, new HashSet<>());
    }

    public Set<String> getFavoriteSerializedSetByKey(String valueToFetch) {
        return getSharedPreferences().getStringSet(valueToFetch, new HashSet<>());
    }

    public void persistUserArtists(String key, Set<String> stringSet) {
        getSharedPreferences().edit().putStringSet(key, stringSet).apply();
    }

    public void persistUserSerializedArtists(String valueToFetch, Set<String> serializedSet) {
        getSharedPreferences().edit().putStringSet(valueToFetch, serializedSet).apply();
    }

    public String getProfilePicUrl() {
        return getSharedPreferences().getString(Constants.PROFILE_PIC, "");
    }
}

