package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.di.annotations.PerFragment;
import com.dihanov.musiq.ui.main.mainfragments.favorites.album.FavoriteAlbums;
import com.dihanov.musiq.ui.main.mainfragments.favorites.album.FavoriteAlbumsContract;
import com.dihanov.musiq.ui.main.mainfragments.favorites.album.FavoriteAlbumsPresenter;
import com.dihanov.musiq.ui.main.mainfragments.favorites.artist.FavoriteArtist;
import com.dihanov.musiq.ui.main.mainfragments.favorites.artist.FavoriteArtistsContract;
import com.dihanov.musiq.ui.main.mainfragments.favorites.artist.FavoriteArtistsPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

@Module
public abstract class FavoriteFragmentModule {
    @Binds
    @PerActivity
    abstract FavoriteArtistsContract.Presenter provideArtistsFragmentPresenter(FavoriteArtistsPresenter presenter);

    @Binds
    @PerActivity
    abstract FavoriteAlbumsContract.Presenter provideAlbumFragmentPresenter(FavoriteAlbumsPresenter presenter);

    @Binds
    @PerFragment
    abstract FavoriteAlbumsContract.View provideAlbumResultFragment(FavoriteAlbums view);

    @Binds
    @PerFragment
    abstract FavoriteArtistsContract.View provideArtistFragment(FavoriteArtist view);
}
