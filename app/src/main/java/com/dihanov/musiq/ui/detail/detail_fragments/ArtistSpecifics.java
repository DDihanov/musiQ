package com.dihanov.musiq.ui.detail.detail_fragments;

import android.content.Context;

import com.dihanov.musiq.ui.detail.ArtistDetails;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

 public abstract class ArtistSpecifics extends DaggerFragment implements ArtistSpecificsContract.View{
    @Inject ArtistSpecificsContract.Presenter artistDetailsFragmentPresenter;
    protected ArtistDetails artistDetailsActivity;

    public ArtistSpecifics() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.artistDetailsActivity = (ArtistDetails) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.artistDetailsFragmentPresenter.leaveView();
    }

    public com.dihanov.musiq.ui.detail.ArtistDetails getArtistDetailsActivity() {
        return artistDetailsActivity;
    }

    protected ArtistSpecificsContract.Presenter getPresenter(){
        return this.artistDetailsFragmentPresenter;
    }
}
