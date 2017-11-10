package com.dihanov.musiq.interfaces;

import com.dihanov.musiq.ui.main.MainActivityContract;

/**
 * Created by dimitar.dihanov on 11/10/2017.
 */

public interface ArtistDetailsIntentShowable {
    void showArtistDetailsIntent(String artistName, MainActivityContract.View mainActivity);
}
