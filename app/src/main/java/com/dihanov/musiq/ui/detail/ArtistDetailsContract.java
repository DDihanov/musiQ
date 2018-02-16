package com.dihanov.musiq.ui.detail;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public interface ArtistDetailsContract {
    interface View extends BaseView<Presenter>, MainViewFunctionable {
        String getArtistBiography();

        String getSerialiedAlbums();

        void setArtist(Artist artist);

        Artist getArtist();
    }

    interface Presenter extends BasePresenter<View> {
    }
}
