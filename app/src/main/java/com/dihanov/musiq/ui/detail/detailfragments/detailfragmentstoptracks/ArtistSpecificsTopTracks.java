package com.dihanov.musiq.ui.detail.detailfragments.detailfragmentstoptracks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistTopTracks;
import com.dihanov.musiq.ui.detail.ArtistDetails;
import com.dihanov.musiq.util.BarChartConfigurator;
import com.github.mikephil.charting.charts.HorizontalBarChart;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * Created by dimitar.dihanov on 2/26/2018.
 */

public class ArtistSpecificsTopTracks extends DaggerFragment implements ArtistSpecificsTopTracksContract.View {
    public static final String TITLE = "artist top tracks";
    @Inject ArtistSpecificsTopTracksContract.Presenter presenter;

    @BindView(R.id.detail_user_artist_tracks_chart)
    HorizontalBarChart barChart;

    @Inject
    BarChartConfigurator barChartConfigurator;

    private ArtistDetails artistDetailsActivity;

    public ArtistSpecificsTopTracks(){
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.artistDetailsActivity = (ArtistDetails) context;
    }

    public static ArtistSpecificsTopTracks newInstance(){
        Bundle args = new Bundle();
        ArtistSpecificsTopTracks artistSpecificsTopTracks = new ArtistSpecificsTopTracks();
        artistSpecificsTopTracks.setArguments(args);
        return artistSpecificsTopTracks;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_top_tracks_fragment, container, false);
        ButterKnife.bind(this, view);

        barChartConfigurator.configureChartColor(barChart);

        this.presenter.takeView(this);
        Artist artist = artistDetailsActivity.getArtist();
        if(artist != null && artist.getName() != null){
            this.presenter.loadArtistTopTracks(artist);
        }

        return view;
    }

    @Override
    public void configureBarChart(ArtistTopTracks artistTopTracks) {
        barChartConfigurator.configureBarChartTrack(artistTopTracks.getToptracks().getTrack(), TITLE, requireActivity(), barChart);
    }

    @Override
    public void showProgressBar() {
        ((ArtistDetails) requireActivity()).showProgressBar();
    }

    @Override
    public void hideProgressBar() {
        ((ArtistDetails) requireActivity()).hideProgressBar();
    }
}
