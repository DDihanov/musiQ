package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.detail.ArtistDetails;
import com.dihanov.musiq.ui.detail.ArtistDetailsContract;
import com.dihanov.musiq.ui.detail.ArtistDetailsPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

@Module
public abstract class ArtistDetailsActivityModule {
    @Binds
    @PerActivity
    abstract ArtistDetailsContract.View provideArtistDetailsActivityView(ArtistDetails artistDetails);

    @Binds
    @PerActivity
    abstract ArtistDetailsContract.Presenter provideArtistResultFragmentPresenter(ArtistDetailsPresenter presenter);
}
