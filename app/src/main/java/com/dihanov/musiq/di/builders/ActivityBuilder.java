package com.dihanov.musiq.di.builders;

import com.dihanov.musiq.di.modules.DetailActivityModule;
import com.dihanov.musiq.di.modules.DetailFragmentModule;
import com.dihanov.musiq.di.modules.MainActivityModule;
import com.dihanov.musiq.ui.detail.DetailActivity;
import com.dihanov.musiq.ui.detail.fragment.DetailFragment;
import com.dihanov.musiq.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = DetailActivityModule.class)
    abstract DetailActivity bindDetailActivity();

    @ContributesAndroidInjector(modules = DetailFragmentModule.class)
    abstract DetailFragment bindDetailFragment();
}
