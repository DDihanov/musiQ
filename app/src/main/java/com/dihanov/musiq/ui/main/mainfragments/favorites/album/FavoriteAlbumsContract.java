package com.dihanov.musiq.ui.main.mainfragments.favorites.album;

import com.dihanov.musiq.interfaces.SpecificAlbumViewHolderClickable;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import java.util.List;
import java.util.Set;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public interface FavoriteAlbumsContract {
    interface View extends BaseView<FavoriteAlbumsContract.Presenter> {
        void showProgressBar();

        void showAlbumDetails(Album fullAlbum);

        void hideProgressBar();

        void setArtistAlbumsList(List<Album> albums);
    }

    interface Presenter extends BasePresenter<FavoriteAlbumsContract.View>, SpecificAlbumViewHolderClickable {
        void loadFavoriteAlbums(Set<String> favorites);
    }
}
