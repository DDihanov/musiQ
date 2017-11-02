package com.dihanov.musiq.ui.detail.detail_fragments;

import com.dihanov.musiq.R;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.interfaces.SpecificAlbumSearchable;
import com.dihanov.musiq.ui.main.AlbumDetailsPopupWindow;
import com.dihanov.musiq.ui.view_holders.AlbumViewHolder;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public class ArtistDetailsFragmentPresenter implements ArtistDetailsFragmentContract.Presenter, SpecificAlbumSearchable {
    @Inject LastFmApiClient lastFmApiClient;

    private ArtistDetailsFragment artistDetailsFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArtistDetailsActivity artistDetailsActivity;

    @Inject
    public ArtistDetailsFragmentPresenter() {
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
        AlbumDetailsPopupWindow albumDetailsPopupWindow = new AlbumDetailsPopupWindow(lastFmApiClient, artistDetailsActivity);
        albumDetailsPopupWindow.showPopupWindow(artistDetailsActivity, viewHolder, artistName, albumName, R.id.detail_content);
    }
}
