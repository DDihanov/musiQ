package com.dihanov.musiq.ui.detail;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class ArtistDetailsPresenter implements ArtistDetailsContract.Presenter {
    private ArtistDetailsContract.View artistDetailsActivity;

    @Inject
    public ArtistDetailsPresenter(){
    }

    @Override
    public void takeView(ArtistDetailsContract.View view) {
        this.artistDetailsActivity = view;
    }

    @Override
    public void leaveView() {
        this.artistDetailsActivity = null;
    }
}
