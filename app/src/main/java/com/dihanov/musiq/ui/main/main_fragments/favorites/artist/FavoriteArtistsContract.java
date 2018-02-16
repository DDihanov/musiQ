package com.dihanov.musiq.ui.main.main_fragments.favorites.artist;

import android.content.Context;

import com.dihanov.musiq.interfaces.RecyclerViewExposable;
import com.dihanov.musiq.interfaces.SpecificArtistSearchable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivity;

import java.util.Set;

public interface FavoriteArtistsContract {
    interface View extends BaseView<Presenter>, RecyclerViewExposable {
        Context getContext();

        MainActivity getMainActivity();
    }

    interface Presenter extends BasePresenter<View>, SpecificArtistSearchable {
        void loadFavoriteArtists(Set<String> favorites, RecyclerViewExposable recyclerViewExposable);
    }
}