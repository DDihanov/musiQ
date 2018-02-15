package com.dihanov.musiq.ui.detail.detail_fragments;

import android.content.Context;

import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivityContract;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

 public abstract class ArtistDetailsFragment extends DaggerFragment implements ArtistDetailsFragmentContract.View{
    @Inject ArtistDetailsFragmentContract.Presenter artistDetailsFragmentPresenter;
    protected ArtistDetailsActivityContract.View artistDetailsActivity;

    public ArtistDetailsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.artistDetailsActivity = (ArtistDetailsActivityContract.View) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.artistDetailsFragmentPresenter.leaveView();
    }

    public ArtistDetailsActivity getArtistDetailsActivity() {
        return (ArtistDetailsActivity)artistDetailsActivity;
    }
}
