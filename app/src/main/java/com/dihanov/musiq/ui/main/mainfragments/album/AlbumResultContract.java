package com.dihanov.musiq.ui.main.mainfragments.album;

import com.dihanov.musiq.interfaces.SpecificAlbumViewHolderClickable;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.GeneralAlbumSearch;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public interface AlbumResultContract {
    interface View extends BaseView<AlbumResultContract.Presenter> {
        void hideProgressBar();

        void hideKeyboard();

        void setSearchResults(GeneralAlbumSearch albumSearchResults);

        void showProgressBar();

        void showAlbumDetails(Album fullAlbum);
    }

    interface Presenter extends BasePresenter<AlbumResultContract.View>, SpecificAlbumViewHolderClickable {
        void searchForAlbum(String albumName);
    }
}
