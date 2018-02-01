package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.di.annotations.PerFragment;
import com.dihanov.musiq.ui.main.main_fragments.favorites.album.FavoriteAlbumsFragment;
import com.dihanov.musiq.ui.main.main_fragments.favorites.album.FavoriteAlbumsFragmentContract;
import com.dihanov.musiq.ui.main.main_fragments.favorites.album.FavoriteAlbumsFragmentPresenter;
import com.dihanov.musiq.ui.main.main_fragments.favorites.artist.FavoriteArtistFragment;
import com.dihanov.musiq.ui.main.main_fragments.favorites.artist.FavoriteArtistsFragmentContract;
import com.dihanov.musiq.ui.main.main_fragments.favorites.artist.FavoriteArtistsFragmentPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

@Module
public abstract class FavoriteFragmentModule {
    @Binds
    @PerActivity
    abstract FavoriteArtistsFragmentContract.Presenter provideArtistsFragmentPresenter(FavoriteArtistsFragmentPresenter presenter);

    @Binds
    @PerActivity
    abstract FavoriteAlbumsFragmentContract.Presenter provideAlbumFragmentPresenter(FavoriteAlbumsFragmentPresenter presenter);

    @Binds
    @PerFragment
    abstract FavoriteAlbumsFragmentContract.View provideAlbumResultFragment(FavoriteAlbumsFragment view);

    @Binds
    @PerFragment
    abstract FavoriteArtistsFragmentContract.View provideArtistFragment(FavoriteArtistFragment view);
}
