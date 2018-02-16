package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.di.annotations.PerFragment;
import com.dihanov.musiq.ui.main.main_fragments.album.AlbumResult;
import com.dihanov.musiq.ui.main.main_fragments.album.AlbumResultContract;
import com.dihanov.musiq.ui.main.main_fragments.album.AlbumResultPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

@Module
public abstract class AlbumResultFragmentModule {
    @Binds
    @PerFragment
    abstract AlbumResultContract.View provideAlbumResultFragmentView(AlbumResult albumResult);

    @Binds
    @PerActivity
    abstract AlbumResultContract.Presenter provideAlbumResultFragmentPresenter(AlbumResultPresenter presenter);
}
