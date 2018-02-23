package com.dihanov.musiq.ui.detail.detail_fragments;

import android.app.Activity;

import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.interfaces.SpecificAlbumSearchable;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.models.UserArtistTracks;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.main.AlbumDetailsPopupWindow;
import com.dihanov.musiq.ui.main.MainContract;
import com.dihanov.musiq.ui.view_holders.AlbumViewHolder;
import com.dihanov.musiq.util.Constants;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public class ArtistSpecificsPresenter implements ArtistSpecificsContract.Presenter, SpecificAlbumSearchable {
    private final LastFmApiClient lastFmApiClient;

    private ArtistSpecificsContract.View artistDetailsFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private com.dihanov.musiq.ui.detail.ArtistDetailsContract.View artistDetailsActivity;

    @Inject
    public ArtistSpecificsPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(ArtistSpecificsContract.View view) {
        this.artistDetailsFragment = (ArtistSpecifics) view;
        this.artistDetailsActivity = artistDetailsFragment.getArtistDetailsActivity();
    }

    @Override
    public void leaveView() {
        this.artistDetailsFragment = null;
        compositeDisposable.clear();
    }


    @Override
    public void setClickListenerFetchEntireAlbumInfo(AlbumViewHolder viewHolder, String artistName, String albumName) {
        AlbumDetailsPopupWindow albumDetailsPopupWindow = new AlbumDetailsPopupWindow(lastFmApiClient, (Activity)artistDetailsActivity);
        albumDetailsPopupWindow.showPopupWindow(artistDetailsActivity, viewHolder, artistName, albumName, R.id.detail_content);
    }
}
