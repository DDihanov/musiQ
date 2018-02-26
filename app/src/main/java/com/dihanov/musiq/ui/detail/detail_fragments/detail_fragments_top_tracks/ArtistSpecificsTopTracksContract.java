package com.dihanov.musiq.ui.detail.detail_fragments.detail_fragments_top_tracks;

import com.dihanov.musiq.interfaces.HorizontalBarChartGettable;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.detail.ArtistDetails;
import com.github.mikephil.charting.charts.HorizontalBarChart;

/**
 * Created by dimitar.dihanov on 2/26/2018.
 */

public interface ArtistSpecificsTopTracksContract {
    interface View extends BaseView<ArtistSpecificsTopTracksContract.Presenter>, HorizontalBarChartGettable{
        HorizontalBarChart getHorizontalBarChart();

        ArtistDetails getDetailActivity();
    }

    interface Presenter extends BasePresenter<ArtistSpecificsTopTracksContract.View>{
        void loadArtistTopTracks(ArtistSpecificsTopTracksContract.View view);

        void setArtist(Artist artist);
    }
}
