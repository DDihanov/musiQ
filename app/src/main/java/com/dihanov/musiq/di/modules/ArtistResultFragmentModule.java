package com.dihanov.musiq.di.modules;

import android.os.Bundle;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.di.annotations.PerFragment;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragment;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragmentContract;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragmentPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Dimitar Dihanov on 02/06/2017.
 */
@Module
public abstract class ArtistResultFragmentModule {
    @Binds
    @PerFragment
    abstract ArtistResultFragmentContract.View provideArtistResultFragmentView(ArtistResultFragment artistResultFragment);

    @Binds
    @PerActivity
    abstract ArtistResultFragmentContract.Presenter provideArtistResultFragmentPresenter(ArtistResultFragmentPresenter presenter);
}
