package com.dihanov.musiq.ui.detail.detailfragments.detailfragmentstoptracks;

import com.dihanov.musiq.data.usecase.GetArtistTopTracksUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistTopTracks;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 2/26/2018.
 */

public class ArtistSpecificsTopTracksPresenter implements
        ArtistSpecificsTopTracksContract.Presenter, UseCase.ResultCallback<ArtistTopTracks> {
    private ArtistSpecificsTopTracksContract.View artistTopTracksView;

    private GetArtistTopTracksUseCase getArtistTopTracksUseCase;

    @Inject
    public ArtistSpecificsTopTracksPresenter(GetArtistTopTracksUseCase getArtistTopTracksUseCase){
        this.getArtistTopTracksUseCase = getArtistTopTracksUseCase;
    }

    @Override
    public void takeView(ArtistSpecificsTopTracksContract.View view) {
        this.artistTopTracksView = view;
    }

    @Override
    public void leaveView() {
        this.artistTopTracksView = null;
    }

    @Override
    public void loadArtistTopTracks(Artist artist) {
        getArtistTopTracksUseCase.invoke(this, artist);
    }

    @Override
    public void onStart() {
        artistTopTracksView.showProgressBar();
    }

    @Override
    public void onSuccess(ArtistTopTracks response) {
        artistTopTracksView.configureBarChart(response);
        artistTopTracksView.hideProgressBar();
    }

    @Override
    public void onError(Throwable e) {
        artistTopTracksView.hideProgressBar();
    }
}
