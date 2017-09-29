package com.dihanov.musiq.ui.detail;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class ArtistDetailsActivityPresenter implements ArtistDetailsActivityContract.Presenter {
    private ArtistDetailsActivityContract.View artistDetailsActivity;

    @Override
    public void takeView(ArtistDetailsActivityContract.View view) {
        this.artistDetailsActivity = view;
    }

    @Override
    public void leaveView() {
        this.artistDetailsActivity = null;
    }
}
