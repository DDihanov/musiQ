package com.dihanov.musiq.ui.detail.detail_fragments;

import com.dihanov.musiq.service.LastFmApiClient;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public class ArtistSpecificsPresenter implements ArtistSpecificsContract.Presenter{
    private final LastFmApiClient lastFmApiClient;

    private ArtistSpecificsContract.View artistDetailsFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ArtistSpecificsPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(ArtistSpecificsContract.View view) {
        this.artistDetailsFragment = view;
    }

    @Override
    public void leaveView() {
        this.artistDetailsFragment = null;
        compositeDisposable.clear();
    }


}
