package com.dihanov.musiq.di.components;

import com.dihanov.musiq.di.modules.DetailFragmentModule;
import com.dihanov.musiq.ui.detail.fragment.DetailFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Subcomponent(modules = DetailFragmentModule.class)
public interface DetailFragmentComponent extends AndroidInjector<DetailFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<DetailFragment>{}
}
