package com.dihanov.musiq.ui.detail;

import com.dihanov.musiq.ui.detail.fragment.DetailFragment;
import com.dihanov.musiq.ui.detail.fragment.DetailFragmentModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


/**
 * Created by mertsimsek on 02/06/2017.
 */
@Module
public abstract class DetailFragmentProvider {

    @ContributesAndroidInjector(modules = DetailFragmentModule.class)
    abstract DetailFragment provideDetailFragmentFactory();
}
