package com.dihanov.musiq.ui.detail.detail_fragments.detail_fragments_top_tracks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dihanov.musiq.R;
import com.dihanov.musiq.ui.detail.ArtistDetails;
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

        configureChart();

        this.presenter.takeView(this);
        this.presenter.setArtist(artistDetailsActivity.getArtist());
        this.presenter.loadArtistTopTracks(this);
        return view;
    }

    private void configureChart() {
        barChart.setNoDataTextColor(R.color.colorPrimary);
    }

    @Override
    public HorizontalBarChart getHorizontalBarChart() {
        return barChart;
    }

    @Override
    public ArtistDetails getDetailActivity() {
        return artistDetailsActivity;
    }
}
