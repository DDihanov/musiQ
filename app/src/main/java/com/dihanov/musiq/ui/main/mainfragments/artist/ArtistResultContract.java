package com.dihanov.musiq.ui.main.mainfragments.artist;

import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public interface ArtistResultContract {
    interface View extends BaseView<Presenter> {
        void hideProgressBar();

        void hideKeyboard();

        void setSearchResults(ArtistSearchResults artistSearchResults);

        void showProgressBar();

        void startActivityWithExtras(HashMap<String, String> bundleExtra);
    }

    interface Presenter extends BasePresenter<View> {
        Observable<ArtistSearchResults> searchForArtist(String artistName);

        void fetchArtist(String artistName);

        void publishResult(Observable<ArtistSearchResults> observable);
    }
}
