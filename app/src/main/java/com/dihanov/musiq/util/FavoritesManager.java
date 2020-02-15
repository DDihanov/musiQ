package com.dihanov.musiq.util;

import com.dihanov.musiq.data.repository.UserSettingsRepository;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by dimitar.dihanov on 2/27/2018.
 */

@Singleton
public class FavoritesManager {
    private UserSettingsRepository userSettingsRepository;

    @Inject
    public FavoritesManager(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    public void addToFavorites(String key, String name, String serializedValue) {
        String valueToFetch =
                key.startsWith(Constants.FAVORITE_ALBUMS_KEY) ? Constants.FAVORITE_ALBUMS_SERIALIZED_KEY : Constants.FAVORITE_ARTISTS_SERIALIZED_KEY;
        Set<String> stringSet = new HashSet<>(userSettingsRepository.getFavoriteSetByKey(key));
        Set<String> serializedSet = new HashSet<>(userSettingsRepository.getFavoriteSerializedSetByKey(valueToFetch));
        stringSet.add(name);
        String serialziedStringToAdd = name + " _$_ " + serializedValue;
        serializedSet.add(serialziedStringToAdd);
        userSettingsRepository.persistUserArtists(key, stringSet);
        userSettingsRepository.persistUserSerializedArtists(valueToFetch, serializedSet);
    }

    public void removeFromFavorites(String key, String value, String serializedValue) {
        String valueToFetch =
                key.startsWith(Constants.FAVORITE_ALBUMS_KEY) ? Constants.FAVORITE_ALBUMS_SERIALIZED_KEY : Constants.FAVORITE_ARTISTS_SERIALIZED_KEY;
        Set<String> stringSet = new HashSet<>(userSettingsRepository.getFavoriteSetByKey(key));
        Set<String> serializedSet = new HashSet<>(userSettingsRepository.getFavoriteSerializedSetByKey(valueToFetch));
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

        userSettingsRepository.persistUserArtists(key, stringSet);
        userSettingsRepository.persistUserSerializedArtists(valueToFetch, serializedSet);
    }

    public boolean isFavorited(String key, String value) {
        Set<String> stringSet = new HashSet<>(userSettingsRepository.getFavoriteSetByKey(key));
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
