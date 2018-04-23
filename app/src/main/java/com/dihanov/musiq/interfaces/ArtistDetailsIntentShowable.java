package com.dihanov.musiq.interfaces;

import com.dihanov.musiq.ui.main.MainContract;

/**
 * Created by dimitar.dihanov on 11/10/2017.
 */

public interface ArtistDetailsIntentShowable {
    void showArtistDetailsIntent(String artistName, MainContract.View mainActivity);

    void showArtistDetailsIntent(String artistName, MainViewFunctionable mainViewFunctionable);
}
