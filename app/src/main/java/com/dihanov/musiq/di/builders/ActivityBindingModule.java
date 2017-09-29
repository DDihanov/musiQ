package com.dihanov.musiq.di.builders;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.di.annotations.PerFragment;
import com.dihanov.musiq.di.modules.ArtistDetailsActivityModule;
import com.dihanov.musiq.di.modules.ArtistResultFragmentModule;
import com.dihanov.musiq.di.modules.MainActivityModule;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.ui.detail.detail_fragments.ArtistDetailsBiographyFragment;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragment;

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

    @PerFragment
    @ContributesAndroidInjector(modules = ArtistResultFragmentModule.class)
    abstract ArtistResultFragment bindArtistResultFragment();

    @PerFragment
    @ContributesAndroidInjector
    abstract ArtistDetailsBiographyFragment bindArtistBiographyFragment();
}
