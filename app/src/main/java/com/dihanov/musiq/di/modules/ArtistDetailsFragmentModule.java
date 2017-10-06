package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerFragment;
import com.dihanov.musiq.ui.detail.detail_fragments.ArtistDetailsFragment;
import com.dihanov.musiq.ui.detail.detail_fragments.ArtistDetailsFragmentContract;
import com.dihanov.musiq.ui.detail.detail_fragments.ArtistDetailsFragmentPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

@Module
public abstract class ArtistDetailsFragmentModule {
    @Binds
    @PerFragment
    abstract ArtistDetailsFragmentContract.View provideArtistDetailsFragmentView(ArtistDetailsFragment artistDetailsFragment);

    @Binds
    @PerFragment
    abstract ArtistDetailsFragmentContract.Presenter provideArtistDetailsFragmentPresenter(ArtistDetailsFragmentPresenter artistDetailsFragmentPresenter);
}
