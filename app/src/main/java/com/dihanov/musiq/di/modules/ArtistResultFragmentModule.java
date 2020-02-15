package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.di.annotations.PerFragment;
import com.dihanov.musiq.ui.main.mainfragments.artist.ArtistResult;
import com.dihanov.musiq.ui.main.mainfragments.artist.ArtistResultContract;
import com.dihanov.musiq.ui.main.mainfragments.artist.ArtistResultPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Dimitar Dihanov on 02/06/2017.
 */
@Module
public abstract class ArtistResultFragmentModule {
    @Binds
    @PerFragment
    abstract ArtistResultContract.View provideArtistResultFragmentView(ArtistResult artistResult);

    @Binds
    @PerActivity
    abstract ArtistResultContract.Presenter provideArtistResultFragmentPresenter(ArtistResultPresenter presenter);
}
