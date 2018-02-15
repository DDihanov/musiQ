package com.dihanov.musiq.ui.detail.detail_fragments;

import android.app.Activity;

import com.dihanov.musiq.R;
import com.dihanov.musiq.interfaces.SpecificAlbumSearchable;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivityContract;
import com.dihanov.musiq.ui.main.AlbumDetailsPopupWindow;
import com.dihanov.musiq.ui.view_holders.AlbumViewHolder;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public class ArtistDetailsFragmentPresenter implements ArtistDetailsFragmentContract.Presenter, SpecificAlbumSearchable {
    private final LastFmApiClient lastFmApiClient;

    private ArtistDetailsFragmentContract.View artistDetailsFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArtistDetailsActivityContract.View artistDetailsActivity;

    @Inject
    public ArtistDetailsFragmentPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(ArtistDetailsFragmentContract.View view) {
        this.artistDetailsFragment = (ArtistDetailsFragment) view;
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
