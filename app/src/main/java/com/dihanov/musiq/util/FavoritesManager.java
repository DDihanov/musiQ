package com.dihanov.musiq.util;

import android.util.ArraySet;

import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.Album;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by dimitar.dihanov on 2/27/2018.
 */

public class FavoritesManager {

    //cannot be instantiated
    private FavoritesManager() {
    }

    public static void addToFavorites(String key, String name, String serializedValue) {
        String valueToFetch =
                key.startsWith(Constants.FAVORITE_ALBUMS_KEY) ? Constants.FAVORITE_ALBUMS_SERIALIZED_KEY : Constants.FAVORITE_ARTISTS_SERIALIZED_KEY;
        Set<String> stringSet = new HashSet<>(App.getSharedPreferences().getStringSet(key, new HashSet<>()));
        Set<String> serializedSet = new HashSet<>(App.getSharedPreferences().getStringSet(valueToFetch, new HashSet<>()));
        stringSet.add(name);
        String serialziedStringToAdd = name + " _$_ " + serializedValue;
        serializedSet.add(serialziedStringToAdd);
        App.getSharedPreferences().edit()
                .putStringSet(key, stringSet)
                .putStringSet(valueToFetch, serializedSet).apply();
    }

    public static void removeFromFavorites(String key, String value, String serializedValue) {
        String valueToFetch =
                key.startsWith(Constants.FAVORITE_ALBUMS_KEY) ? Constants.FAVORITE_ALBUMS_SERIALIZED_KEY : Constants.FAVORITE_ARTISTS_SERIALIZED_KEY;
        Set<String> stringSet = new HashSet<>(App.getSharedPreferences().getStringSet(key, new HashSet<>()));
        Set<String> serializedSet = new HashSet<>(App.getSharedPreferences().getStringSet(valueToFetch, new HashSet<>()));
        Iterator<String> it = stringSet.iterator();
        while (it.hasNext()) {
            String entry = it.next();
            if (entry.equals(value)) {
                it.remove();
                break;
            }
        }

        Iterator<String> it2 = serializedSet.iterator();
        while (it2.hasNext()) {
            String entry = it2.next();
            if (entry.startsWith(value)) {
                it2.remove();
                break;
            }
        }

        App.getSharedPreferences().edit()
                .putStringSet(key, stringSet)
                .putStringSet(valueToFetch, serializedSet)
                .apply();
    }

    public static boolean isFavorited(String key, String value) {
        Set<String> stringSet = new HashSet<>(App.getSharedPreferences().getStringSet(key, new HashSet<>()));
        boolean contains = false;
        for (Iterator<String> i = stringSet.iterator(); i.hasNext(); ) {
            String entry = i.next();
            if (entry.equals(value)) {
                contains = true;
                break;
            }
        }

        return contains;
    }
}
