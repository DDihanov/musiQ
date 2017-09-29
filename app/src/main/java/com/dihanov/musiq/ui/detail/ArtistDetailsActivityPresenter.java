package com.dihanov.musiq.ui.detail;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class ArtistDetailsActivityPresenter implements ArtistDetailsActivityContract.Presenter {
    private ArtistDetailsActivityContract.View artistDetailsActivity;

    @Inject
    public ArtistDetailsActivityPresenter(){

    }

    @Override
    public void takeView(ArtistDetailsActivityContract.View view) {
        this.artistDetailsActivity = view;
    }

    @Override
    public void leaveView() {
        this.artistDetailsActivity = null;
    }


    //using blockingGet, because the lastFM service does not return all artist details when searching for
    //artists using the searchForArtist method, so I have to search for artist once again, but this time
    //using getSpecificArtistInfo. So I use a blocking get, so my activity can wait for all the details
    //to load first, otherwise I will have to implement threads checking if the artist detail
    //has been loaded before loading it.

}
