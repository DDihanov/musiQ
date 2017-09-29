package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivityContract;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivityPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

@Module
public abstract class ArtistDetailsActivityModule {
    @Binds
    @PerActivity
    abstract ArtistDetailsActivityContract.View provideArtistDetailsActivityView(ArtistDetailsActivity artistDetailsActivity);

    @Binds
    @PerActivity
    abstract ArtistDetailsActivityContract.Presenter provideArtistResultFragmentPresenter(ArtistDetailsActivityPresenter presenter);
}
