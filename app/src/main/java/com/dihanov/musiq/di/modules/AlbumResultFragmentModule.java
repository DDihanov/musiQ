package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.di.annotations.PerFragment;
import com.dihanov.musiq.ui.main.main_fragments.album.AlbumResultFragment;
import com.dihanov.musiq.ui.main.main_fragments.album.AlbumResultFragmentContract;
import com.dihanov.musiq.ui.main.main_fragments.album.AlbumResultFragmentPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

@Module
public abstract class AlbumResultFragmentModule {
    @Binds
    @PerFragment
    abstract AlbumResultFragmentContract.View provideAlbumResultFragmentView(AlbumResultFragment albumResultFragment);

    @Binds
    @PerActivity
    abstract AlbumResultFragmentContract.Presenter provideAlbumResultFragmentPresenter(AlbumResultFragmentPresenter presenter);
}
