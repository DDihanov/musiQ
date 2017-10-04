package com.dihanov.musiq.ui.detail;

import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import java.util.List;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public interface ArtistDetailsActivityContract {
    interface View extends BaseView<Presenter> {
        String getArtistBiography();

        void setArtist(Artist artist);

        Artist getArtist();

        List<Album> getAlbums();
    }

    interface Presenter extends BasePresenter<View> {
    }
}
