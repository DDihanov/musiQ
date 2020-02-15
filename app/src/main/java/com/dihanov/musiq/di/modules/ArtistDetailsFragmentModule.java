package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.detail.detailfragments.ArtistSpecifics;
import com.dihanov.musiq.ui.detail.detailfragments.ArtistSpecificsContract;
import com.dihanov.musiq.ui.detail.detailfragments.ArtistSpecificsPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

@Module
public abstract class ArtistDetailsFragmentModule {
    @Binds
    @PerActivity
    abstract ArtistSpecificsContract.View provideArtistDetailsFragmentView(ArtistSpecifics artistSpecifics);

    @Binds
    @PerActivity
    abstract ArtistSpecificsContract.Presenter provideArtistDetailsFragmentPresenter(ArtistSpecificsPresenter artistSpecificsPresenter);
}
