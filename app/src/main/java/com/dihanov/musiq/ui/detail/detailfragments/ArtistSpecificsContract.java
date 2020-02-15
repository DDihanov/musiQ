package com.dihanov.musiq.ui.detail.detailfragments;

import com.dihanov.musiq.interfaces.SpecificAlbumViewHolderClickable;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public interface ArtistSpecificsContract {
    interface View extends BaseView<ArtistSpecificsContract.Presenter>{
        void showProgressBar();

        void showAlbumDetails(Album fullAlbum);

        void hideProgressBar();
    }

    interface Presenter extends BasePresenter<ArtistSpecificsContract.View>, SpecificAlbumViewHolderClickable {
    }
}
