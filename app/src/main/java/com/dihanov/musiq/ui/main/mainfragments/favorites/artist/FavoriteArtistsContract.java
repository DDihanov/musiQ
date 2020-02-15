package com.dihanov.musiq.ui.main.mainfragments.favorites.artist;

import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface FavoriteArtistsContract {
    interface View extends BaseView<Presenter> {
        void hideProgressBar();

        void showProgressBar();

        void setArtistList(List<Artist> artists);

        void startActivityWithExtras(HashMap<String, String> bundleExtra);
    }

    interface Presenter extends BasePresenter<View> {
        void loadFavoriteArtists(Set<String> favorites);

        void fetchArtist(String artistName);
    }
}