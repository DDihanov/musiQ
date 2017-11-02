package com.dihanov.musiq.di.builders;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.di.modules.AlbumResultFragmentModule;
import com.dihanov.musiq.di.modules.ArtistDetailsActivityModule;
import com.dihanov.musiq.di.modules.ArtistDetailsFragmentModule;
import com.dihanov.musiq.di.modules.ArtistResultFragmentModule;
import com.dihanov.musiq.di.modules.MainActivityModule;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.ui.detail.detail_fragments.ArtistDetailsAlbumFragment;
import com.dihanov.musiq.ui.detail.detail_fragments.ArtistDetailsBiographyFragment;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.main_fragments.album.AlbumResultFragment;
import com.dihanov.musiq.ui.main.main_fragments.artist.ArtistResultFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public abstract class ActivityBindingModule {
    @PerActivity
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ArtistDetailsActivityModule.class)
    abstract ArtistDetailsActivity bindArtistDetailActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ArtistResultFragmentModule.class)
    abstract ArtistResultFragment bindArtistResultFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = AlbumResultFragmentModule.class)
    abstract AlbumResultFragment bindAlbumResultFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = ArtistDetailsFragmentModule.class)
    abstract ArtistDetailsBiographyFragment bindArtistBiographyFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = ArtistDetailsFragmentModule.class)
    abstract ArtistDetailsAlbumFragment bindArtistDetailsAlbumsFragment();
}
