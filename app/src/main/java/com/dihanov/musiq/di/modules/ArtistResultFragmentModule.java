package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragment;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragmentContract;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragmentPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Dimitar Dihanov on 02/06/2017.
 */
@Module
public abstract class ArtistResultFragmentModule {
    @Binds
    abstract ArtistResultFragmentContract.Presenter provideArtistResultFragmentPresenter(ArtistResultFragmentPresenter presenter);

    @Binds
    abstract ArtistResultFragmentContract.View provideArtistResultFragmentView(ArtistResultFragment artistResultFragment);
}
