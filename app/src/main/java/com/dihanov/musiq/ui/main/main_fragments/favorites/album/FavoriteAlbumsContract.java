package com.dihanov.musiq.ui.main.main_fragments.favorites.album;

import android.content.Context;

import com.dihanov.musiq.interfaces.RecyclerViewExposable;
import com.dihanov.musiq.interfaces.SpecificAlbumSearchable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivity;

import java.util.Set;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public interface FavoriteAlbumsContract {
    interface View extends BaseView<FavoriteAlbumsContract.Presenter>, RecyclerViewExposable {
        Context getContext();

        MainActivity getMainActivity();
    }

    interface Presenter extends BasePresenter<FavoriteAlbumsContract.View>, SpecificAlbumSearchable {
        void loadFavoriteAlbums(Set<String> favorites, RecyclerViewExposable recyclerViewExposable);
    }
}
