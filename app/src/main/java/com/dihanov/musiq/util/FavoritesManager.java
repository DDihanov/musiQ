package com.dihanov.musiq.util;

import com.dihanov.musiq.di.app.App;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by dimitar.dihanov on 2/27/2018.
 */

public class FavoritesManager {

    //cannot be instantialized
    private FavoritesManager() {
    }

    public static void addToFavorites(String key, String value){
        Set<String> stringSet = App.getSharedPreferences().getStringSet(key, new HashSet<>());
        stringSet.add(value);
        App.getSharedPreferences().edit().putStringSet(key, stringSet).apply();
    }

    public static void removeFromFavorites(String key, String value){
        Set<String> stringSet = App.getSharedPreferences().getStringSet(key, new HashSet<>());
        Iterator<String> it = stringSet.iterator();
        while(it.hasNext()){
            String entry = it.next();
            if(entry.equals(value)){
                it.remove();
                break;
            }
        }

        App.getSharedPreferences().edit().putStringSet(key, stringSet).apply();
    }

    public static boolean isFavorited(String key, String value){
        Set<String> stringSet = App.getSharedPreferences().getStringSet(key, new HashSet<>());
        boolean contains = false;
        for (Iterator<String> i = stringSet.iterator(); i.hasNext(); ) {
            String entry = i.next();
            if(entry.equals(value)){
                contains = true;
                break;
            }
        }

        return contains;
    }
}
