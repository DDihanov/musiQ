package com.dihanov.musiq.di.modules;

import android.os.Bundle;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.di.annotations.PerFragment;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragment;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragmentContract;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragmentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dimitar Dihanov on 02/06/2017.
 */
@Module
public class ArtistResultFragmentModule {
    @Provides
    @PerFragment
    ArtistResultFragmentContract.View provideArtistResultFragmentView(ArtistResultFragment artistResultFragment){
        return artistResultFragment;
    }

    @Provides
    @PerActivity
    ArtistResultFragmentContract.Presenter provideArtistResultFragmentPresenter(ArtistResultFragmentPresenter presenter){
        return presenter;
    }
}
