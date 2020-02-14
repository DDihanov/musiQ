package com.dihanov.musiq.ui.detail.detail_fragments.detail_fragments_top_tracks;

import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistTopTracks;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 2/26/2018.
 */

public interface ArtistSpecificsTopTracksContract {
    interface View extends BaseView<ArtistSpecificsTopTracksContract.Presenter> {
        void configureBarChart(ArtistTopTracks artistTopTracks);

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter extends BasePresenter<ArtistSpecificsTopTracksContract.View>{
        void loadArtistTopTracks(Artist artist);
    }
}
